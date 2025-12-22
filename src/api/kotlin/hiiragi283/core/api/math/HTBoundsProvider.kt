package hiiragi283.core.api.math

/**
 * [範囲][HTBounds]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTBoundsProvider {
    /**
     * 保持している[範囲][HTBounds]を返します。
     */
    fun getBounds(): HTBounds

    /**
     * 指定した[x], [y]がこの[範囲][getBounds]に含まれているか判定します。
     */
    fun isHovered(x: Int, y: Int): Boolean = getBounds().contains(x, y)
}
