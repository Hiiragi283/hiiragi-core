package hiiragi283.core.api.material.property

import hiiragi283.core.api.div
import hiiragi283.core.api.fraction
import hiiragi283.core.api.times
import org.apache.commons.lang3.math.Fraction

/**
 * 素材のランクに使用されるクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
enum class HTMaterialLevel(val timeMultiplier: Fraction?) {
    NONE(null),
    LOW(Fraction.ONE_HALF),
    MEDIUM(Fraction.ONE),
    HIGH(fraction(2)),
    HIGHEST(fraction(3)),
    ;

    operator fun times(value: Int): Fraction? = timeMultiplier?.let(value::times)

    operator fun div(value: Int): Fraction? = timeMultiplier?.let(value::div)
}
