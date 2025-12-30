package hiiragi283.core.api.storage.resource

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import kotlin.math.min

/**
 * リソースを搬入/搬出できることを表すインターフェースです。
 * @param RESOURCE 保持するリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTResourceSlot<RESOURCE : HTResourceType<*>> :
    HTResourceView<RESOURCE>,
    HTValueSerializable,
    HTContentListener {
    /**
     * 指定した[resource]が有効か判定します。
     * @return 有効な場合は`true`
     */
    fun isValid(resource: RESOURCE): Boolean

    /**
     * このスロットにリソースを搬入します。
     * @param resource 搬入するリソース
     * @param amount 搬入する量
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬入されない数量
     */
    fun insert(
        resource: RESOURCE?,
        amount: Int,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int

    /**
     * このスロットからリソースを搬出します。
     * @param resource 搬出するリソース
     * @param amount 搬出する量
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬出される数量
     */
    fun extract(
        resource: RESOURCE?,
        amount: Int,
        action: HTStorageAction,
        access: HTStorageAccess,
    ): Int = when (resource) {
        null -> 0
        getResource() -> extract(amount, action, access)
        else -> 0
    }

    /**
     * このスロットからリソースを搬出します。
     * @param amount 搬出する量
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬出される数量
     */
    fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int

    //    Basic    //

    abstract class Basic<RESOURCE : HTResourceType<*>> : HTResourceSlot<RESOURCE> {
        /**
         * 指定した[resource]で中身を置換します。
         */
        abstract fun setResource(resource: RESOURCE?)

        /**
         * 保持しているリソースの量を変更します。
         * @param amount 新しい量
         */
        abstract fun setAmount(amount: Int)

        /**
         * 保持しているリソースの量を追加します。
         * @param amount 追加する量
         */
        protected fun growAmount(amount: Int) {
            setAmount(this.getAmount() + amount)
        }

        /**
         * 保持しているリソースの量を減少します。
         * @param amount 減少する量
         */
        protected fun shrinkAmount(amount: Int) {
            setAmount(this.getAmount() - amount)
        }

        override fun insert(
            resource: RESOURCE?,
            amount: Int,
            action: HTStorageAction,
            access: HTStorageAccess,
        ): Int {
            if (resource == null || amount <= 0) return 0
            val needed: Int = min(inputRate(access), getNeeded(resource))
            if (needed <= 0 || !isStackValidForInsert(resource, access)) return amount

            val resourceIn: RESOURCE? = this.getResource()
            val sameType: Boolean = resource == resourceIn
            if (resourceIn == null || sameType) {
                val toAdd: Int = min(amount, needed)
                if (action.execute()) {
                    if (sameType) {
                        growAmount(toAdd)
                        onContentsChanged()
                    } else {
                        setResource(resource)
                        setAmount(toAdd)
                    }
                }
                return amount - toAdd
            }
            return amount
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            val resourceIn: RESOURCE? = this.getResource()
            if (resourceIn == null || amount < 1 || !canStackExtract(resourceIn, access)) return 0
            val fixedAmount: Int = min(min(outputRate(access), getAmount()), amount)
            if (fixedAmount > 0 && action.execute()) {
                shrinkAmount(fixedAmount)
                onContentsChanged()
            }
            return fixedAmount
        }

        /**
         * 指定したリソースをこのスロットに搬入できるか判定します。
         * @param resource 搬入されるリソース
         * @param access このスロットへのアクセスの種類
         * @return 搬入できる場合は`true`
         */
        open fun isStackValidForInsert(resource: RESOURCE, access: HTStorageAccess): Boolean = isValid(resource)

        /**
         * 指定したリソースをこのスロットから搬出できるか判定します。
         * @param resource 搬出されるリソース
         * @param access このスロットへのアクセスの種類
         * @return 搬出できる場合は`true`
         */
        open fun canStackExtract(resource: RESOURCE, access: HTStorageAccess): Boolean = true

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
