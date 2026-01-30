package hiiragi283.core.api.storage.amount

import hiiragi283.core.api.fixedFraction
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.resource.HTResourceView
import org.apache.commons.lang3.math.Fraction

/**
 * 量と容量を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 * @see HTAmountSlot
 * @see HTResourceView
 */
interface HTAmountView {
    /**
     * 保持している量を返します。
     */
    fun getAmount(): Int

    /**
     * 容量を返します。
     */
    fun getCapacity(): Int

    /**
     * 空き容量を返します。
     */
    fun getNeeded(): Int = maxOf(0, getCapacity() - getAmount())

    /**
     * 占有率を返します。
     * @return [Fraction]型での占有率
     */
    fun getLevelAsFraction(): Fraction = fixedFraction(getAmount(), getCapacity())

    /**
     * 占有率を返します。
     * @return [Float]型での占有率
     */
    fun getLevelAsFloat(): Float = getLevelAsFraction().toFloat()

    /**
     * このビューが空かどうか判定します。
     * @since 0.9.0
     */
    fun isEmpty(): Boolean = getAmount() <= 0

    //    Mutable    //

    abstract class Mutable : HTAmountView {
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
