package hiiragi283.core.api.storage.amount

import com.google.common.primitives.Ints
import hiiragi283.core.api.math.fixedFraction
import hiiragi283.core.api.storage.stack.HTStackView
import org.apache.commons.lang3.math.Fraction
import kotlin.math.max

/**
 * 量と容量を保持するインターフェースです。
 * @param N 保持する数値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTAmountSlot
 * @see HTStackView
 */
sealed interface HTAmountView<N> where N : Number, N : Comparable<N> {
    /**
     * 保持している量を返します。
     */
    fun getAmount(): N

    /**
     * 容量を返します。
     */
    fun getCapacity(): N

    /**
     * 空き容量を返します。
     */
    fun getNeeded(): N

    /**
     * 占有率を返します。
     * @return [Fraction]型での占有率
     */
    fun getStoredLevel(): Fraction

    /**
     * [Int]値を扱う[HTAmountView]の拡張インターフェース
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    interface IntSized : HTAmountView<Int> {
        override fun getNeeded(): Int = max(0, getCapacity() - getAmount())

        override fun getStoredLevel(): Fraction = fixedFraction(getAmount(), getCapacity())
    }

    /**
     * [Long]値を扱う[HTAmountView]の拡張インターフェース
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    interface LongSized : HTAmountView<Long> {
        /**
         * 個数を[Int]型で取得します。
         */
        fun getAmountAsInt(): Int = Ints.saturatedCast(getAmount())

        /**
         * 容量を[Int]型で取得します。
         */
        fun getCapacityAsInt(): Int = Ints.saturatedCast(getCapacity())

        override fun getNeeded(): Long = max(0, getCapacity() - getAmount())

        override fun getStoredLevel(): Fraction = fixedFraction(getAmount(), getCapacity())
    }
}
