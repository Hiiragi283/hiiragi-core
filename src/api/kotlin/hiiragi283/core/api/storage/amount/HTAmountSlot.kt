package hiiragi283.core.api.storage.amount

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import kotlin.math.min

/**
 * 量を搬入/搬出できることを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
interface HTAmountSlot : HTAmountView {
    /**
     * このスロットが空かどうか判定します。
     */
    fun isEmpty(): Boolean = getAmount() <= 0

    /**
     * このスロットに量を搬入します。
     * @param amount 搬入する量
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬入される量
     */
    fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int

    /**
     * このスロットから量を搬出します。
     * @param amount 搬出する量
     * @param action 処理のフラグ
     * @param access このスロットへのアクセスの種類
     * @return 搬出される量
     */
    fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int

    //    Basic    //

    abstract class Basic :
        HTAmountView.Mutable(),
        HTAmountSlot {
        override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (amount <= 0 || !canInsert(access)) return 0
            val needed: Int = min(inputRate(access), getNeeded())
            if (needed <= 0) return 0
            val toAdd: Int = min(amount, needed)
            if (action.execute()) {
                setAmount(getAmount() + toAdd)
            }
            return toAdd
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (isEmpty() || amount <= 0 || !canExtract(access)) return 0
            val toRemove: Int = min(min(outputRate(access), getAmount()), amount)
            if (toRemove > 0 && action.execute()) {
                setAmount(getAmount() - toRemove)
            }
            return toRemove
        }

        /**
         * このスロットに搬入できるか判定します。
         * @param access このスロットへのアクセスの種類
         * @return 搬入できる場合は`true`
         */
        protected open fun canInsert(access: HTStorageAccess): Boolean = true

        /**
         * このスロットから搬出できるか判定します。
         * @param access このスロットへのアクセスの種類
         * @return 搬出できる場合は`true`
         */
        protected open fun canExtract(access: HTStorageAccess): Boolean = true
    }
}
