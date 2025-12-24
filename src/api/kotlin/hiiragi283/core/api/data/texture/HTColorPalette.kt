package hiiragi283.core.api.data.texture

import java.awt.Color

/**
 * カラーパレットを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun interface HTColorPalette {
    operator fun get(index: Int): Color
}
