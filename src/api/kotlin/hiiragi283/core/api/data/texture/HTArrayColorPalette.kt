package hiiragi283.core.api.data.texture

import java.awt.Color

/**
 * カラーパレットを表すクラスです。
 * @param colors 色の配列
 */
@JvmInline
value class HTArrayColorPalette(val colors: Array<Color>) : HTColorPalette {
    override fun get(index: Int): Color = colors[index]
}
