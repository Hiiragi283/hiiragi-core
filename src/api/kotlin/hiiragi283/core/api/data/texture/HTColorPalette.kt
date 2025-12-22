package hiiragi283.core.api.data.texture

import java.awt.Color

/**
 * カラーパレットを表すクラスです。
 * @param colors 色の配列
 */
@JvmInline
value class HTColorPalette(val colors: Array<Color>) : Collection<Color> {
    operator fun get(index: Int): Color = colors[index]

    override val size: Int
        get() = colors.size

    override fun isEmpty(): Boolean = colors.isEmpty()

    override fun contains(element: Color): Boolean = colors.contains(element)

    override fun iterator(): Iterator<Color> = colors.iterator()

    override fun containsAll(elements: Collection<Color>): Boolean = elements.all { contains(it) }
}
