package hiiragi283.core.api.data.texture

import java.awt.Color

/**
 * [Color]の配列に基づいた[HTColorPalette]の実装クラスです。。
 * @param colors 色の配列
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
@JvmInline
value class HTArrayColorPalette(val colors: Array<Color>) : HTColorPalette {
    override fun get(index: Int): Color = colors[index]
}
