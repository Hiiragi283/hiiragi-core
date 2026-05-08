package hiiragi283.core.api.material.part.property

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.toFraction
import org.apache.commons.lang3.math.Fraction

/**
 * @since 0.8.0
 */
fun HTPartLike.getScaledAmount(base: Int, getter: HTPropertyGetter): Fraction = this.getScaledAmount(base.toFraction(1), getter)

/**
 * @since 0.8.0
 */
fun HTPartLike.getScaledAmount(base: Float, getter: HTPropertyGetter): Fraction = this.getScaledAmount(base.toFraction(), getter)

/**
 * @since 0.8.0
 */
fun HTPartLike.getScaledAmount(base: Fraction, getter: HTPropertyGetter): Fraction = this.getOrDefault(HTPartPropertyKeys.ITEM_SCALE)(base, getter)

// Mutable

fun HTPropertyMap.Builder.addNamePattern(enPattern: String, jaPattern: String) {
    this.addNamePattern(HTLangPatternProvider.create(enPattern, jaPattern))
}

fun HTPropertyMap.Builder.addNamePattern(value: HTLangPatternProvider) {
    this[HTPartPropertyKeys.LANG_PATTERN] = value
}
