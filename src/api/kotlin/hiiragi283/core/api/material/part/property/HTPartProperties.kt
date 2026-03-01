package hiiragi283.core.api.material.part.property

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.toFraction
import org.apache.commons.lang3.math.Fraction

/**
 * @since 0.8.0
 */
fun HTPartLike.getScaledAmount(base: Int, propertyMap: HTPropertyMap): Fraction = this.getScaledAmount(base.toFraction(1), propertyMap)

/**
 * @since 0.8.0
 */
fun HTPartLike.getScaledAmount(base: Float, propertyMap: HTPropertyMap): Fraction = this.getScaledAmount(base.toFraction(), propertyMap)

/**
 * @since 0.8.0
 */
fun HTPartLike.getScaledAmount(base: Fraction, propertyMap: HTPropertyMap): Fraction =
    this.getOrDefault(HTPartPropertyKeys.ITEM_SCALE)(base, propertyMap)

// Mutable

fun HTPropertyMap.Mutable.addNamePattern(enPattern: String, jaPattern: String) {
    this.addNamePattern(HTLangPatternProvider.create(enPattern, jaPattern))
}

fun HTPropertyMap.Mutable.addNamePattern(value: HTLangPatternProvider) {
    this[HTPartPropertyKeys.LANG_PATTERN] = value
}
