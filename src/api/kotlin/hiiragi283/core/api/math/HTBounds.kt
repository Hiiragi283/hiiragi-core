package hiiragi283.core.api.math

/**
 * 二次元平面上の範囲を表現するクラスです。
 * @param x x軸方向の始点
 * @param y y軸方向の始点
 * @param width x軸方向の長さ
 * @param height y軸方向の長さ
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see dev.emi.emi.api.widget.Bounds
 */
@JvmRecord
data class HTBounds(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
) {
    companion object {
        @JvmField
        val EMPTY = HTBounds(0, 0, 0, 0)

        @JvmStatic
        fun createSlot(x: Int, y: Int): HTBounds = HTBounds(x, y, 18, 18)
    }

    /**
     * x軸方向の範囲の最小値
     */
    val left: Int get() = x

    /**
     * x軸方向の範囲の最大値
     */
    val right: Int get() = x + width

    /**
     * y軸方向の範囲の最小値
     */
    val top: Int get() = y

    /**
     * y軸方向の範囲の最大値
     */
    val bottom: Int get() = y + height

    /**
     * [left]を起点とするx軸方向の範囲
     */
    val widthRange: IntRange get() = (left..<right)

    /**
     * [top]を起点とするy軸方向の範囲
     */
    val heightRange: IntRange get() = (top..<bottom)

    /**
     * 指定した[x], [y]がこの範囲に含まれているか判定します。
     */
    fun contains(x: Int, y: Int): Boolean = x in widthRange && y in heightRange
}
