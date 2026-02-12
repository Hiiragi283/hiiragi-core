package hiiragi283.core.api.material.property

import hiiragi283.core.api.fraction
import org.apache.commons.lang3.math.Fraction

/**
 * 素材のランクに使用されるクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
enum class HTMaterialLevel(val timeMultiplier: Fraction) {
    NONE(Fraction.ZERO),
    LOW(Fraction.ONE_HALF),
    MEDIUM(Fraction.ONE),
    HIGH(fraction(2)),
    HIGHEST(fraction(3)),
}
