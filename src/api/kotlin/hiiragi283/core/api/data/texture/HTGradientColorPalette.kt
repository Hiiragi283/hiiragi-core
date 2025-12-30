package hiiragi283.core.api.data.texture

import hiiragi283.core.api.math.fraction
import hiiragi283.core.api.math.plus
import hiiragi283.core.api.math.times
import org.apache.commons.lang3.math.Fraction
import java.awt.Color

/**
 * RGBによるグラデーションに基づいた[HTColorPalette]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see mekanism.common.lib.Color.ColorFunction.scale
 */
class HTGradientColorPalette(val from: Color, val to: Color) : HTColorPalette {
    override fun get(index: Int): Color {
        val fraction: Fraction = fraction(index, 5)
        val alpha: Fraction = from.alpha + (to.alpha - from.alpha) * fraction
        val red: Fraction = from.red + (to.red - from.red) * fraction
        val green: Fraction = from.green + (to.green - from.green) * fraction
        val blue: Fraction = from.blue + (to.blue - from.blue) * fraction
        return Color(red.toInt(), green.toInt(), blue.toInt(), alpha.toInt())
    }
}
