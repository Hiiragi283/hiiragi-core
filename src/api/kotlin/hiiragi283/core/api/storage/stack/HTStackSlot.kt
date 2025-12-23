package hiiragi283.core.api.storage.stack

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.stack.ImmutableStack
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import kotlin.math.min

/**
 * スタックを搬入/搬出できることを表すインターフェースです。
 * @param STACK 保持するスタックのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTStackSlot<STACK : ImmutableStack<*, STACK>> :
    HTStackView<STACK>,
    HTValueSerializable,
    HTContentListener {
    /**
     * 指定した[stack]が有効か判定します。
     * @return 有効な場合は`true`
     */
    fun isValid(stack: STACK): Boolean

    /**
     * このスロットにスタックを搬入します。
     * @param stack 搬入するスタック
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬入されないスタック
     */
    fun insert(stack: STACK?, action: HTStorageAction, access: HTStorageAccess): STACK?

    /**
     * このスロットからスタックを搬出します。
     * @param stack 搬出するスタック
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬出されるスタック
     */
    fun extract(stack: STACK?, action: HTStorageAction, access: HTStorageAccess): STACK? = when {
        stack == null -> null
        isSameStack(stack) -> extract(stack.amount(), action, access)
        else -> null
    }

    /**
     * このスロットからスタックを搬出します。
     * @param amount 搬出する量
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬出されるスタック
     */
    fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): STACK?

    /**
     * 指定した[other]が現在の[スタック][getStack]と等価か判定します。
     */
    fun isSameStack(other: STACK?): Boolean

    //    Basic    //

    /**
     * [HTStackSlot]の基本的な実装クラスです。
     * @param STACK 保持するスタックのクラス
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    abstract class Basic<STACK : ImmutableStack<*, STACK>> : HTStackSlot<STACK> {
        /**
         * 指定した[stack]で中身を置換します。
         */
        abstract fun setStack(stack: STACK?)

        /**
         * 保持しているスタックの量を変更します。
         * @param amount 新しい量
         */
        protected abstract fun updateAmount(amount: Int)

        /**
         * 保持しているスタックの量を追加します。
         * @param amount 追加する量
         */
        protected fun growAmount(stack: STACK, amount: Int) {
            updateAmount(stack.amount() + amount)
        }

        /**
         * 保持しているスタックの量を減少します。
         * @param amount 減少する量
         */
        protected fun shrinkAmount(stack: STACK, amount: Int) {
            updateAmount(stack.amount() - amount)
        }

        /**
         * @see mekanism.common.inventory.slot.BasicInventorySlot.insertItem
         * @see mekanism.common.capabilities.fluid.BasicFluidTank.insert
         */
        override fun insert(stack: STACK?, action: HTStorageAction, access: HTStorageAccess): STACK? {
            if (stack == null) return null
            val needed: Int = min(inputRate(access), getNeeded(stack))
            if (needed <= 0 || !isStackValidForInsert(stack, access)) return stack

            val stackIn: STACK? = this.getStack()
            val sameType: Boolean = isSameStack(stack)
            if (stackIn == null || sameType) {
                val toAdd: Int = min(stack.amount(), needed)
                if (action.execute()) {
                    if (sameType && stackIn != null) {
                        growAmount(stackIn, toAdd)
                        onContentsChanged()
                    } else {
                        setStack(stack.copyWithAmount(toAdd))
                    }
                }
                return stack.copyWithAmount(stack.amount() - toAdd)
            }
            return stack
        }

        /**
         * @see mekanism.common.inventory.slot.BasicInventorySlot.extractItem
         * @see mekanism.common.capabilities.fluid.BasicFluidTank.extract
         */
        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): STACK? {
            val stack: STACK? = this.getStack()
            if (stack == null || amount < 1 || !canStackExtract(stack, access)) return null
            val fixedAmount: Int = min(min(outputRate(access), getAmount()), amount)
            val result: STACK? = stack.copyWithAmount(fixedAmount)
            if (result != null && action.execute()) {
                shrinkAmount(stack, fixedAmount)
                onContentsChanged()
            }
            return result
        }

        /**
         * 指定した[stack]がこのスロットに搬入できるか判定します。
         * @param stack 搬入されるスタック
         * @param access このスロットへのアクセスの種類
         * @return 搬入できる場合は`true`
         */
        open fun isStackValidForInsert(stack: STACK, access: HTStorageAccess): Boolean = isValid(stack)

        /**
         * 指定した[stack]をこのスロットから搬出できるか判定します。
         * @param stack 搬出されるスタック
         * @param access このスロットへのアクセスの種類
         * @return 搬出できる場合は`true`
         */
        open fun canStackExtract(stack: STACK, access: HTStorageAccess): Boolean = true

        /**
         * 一度に搬入される量の上限を返します。
         * @param access このスロットへのアクセスの種類
         */
        protected open fun inputRate(access: HTStorageAccess): Int = Int.MAX_VALUE

        /**
         * 一度に搬出される量の上限を返します。
         * @param access このスロットへのアクセスの種類
         */
        protected open fun outputRate(access: HTStorageAccess): Int = Int.MAX_VALUE
    }
}
