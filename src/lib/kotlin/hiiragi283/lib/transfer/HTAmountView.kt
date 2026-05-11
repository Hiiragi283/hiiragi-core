package hiiragi283.lib.transfer

import com.google.common.primitives.Ints

/**
 * 量を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 * @see HTResourceView
 */
fun interface HTAmountView {
    fun getAmountAsLong(): Long

    fun getAmountAsInt(): Int = Ints.saturatedCast(getAmountAsLong())
}
