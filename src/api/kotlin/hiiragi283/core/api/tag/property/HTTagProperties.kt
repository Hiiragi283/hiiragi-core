package hiiragi283.core.api.tag.property

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.toFraction
import org.apache.commons.lang3.math.Fraction

/**
 * @since 0.8.0
 */
fun HTTagPrefix.getScaledAmount(base: Int, propertyMap: HTPropertyMap): Fraction = this.getScaledAmount(base.toFraction(1), propertyMap)

/**
 * @since 0.8.0
 */
fun HTTagPrefix.getScaledAmount(base: Float, propertyMap: HTPropertyMap): Fraction = this.getScaledAmount(base.toFraction(), propertyMap)

/**
 * @since 0.8.0
 */
fun HTTagPrefix.getScaledAmount(base: Fraction, propertyMap: HTPropertyMap): Fraction =
    this.getOrDefault(HTTagPropertyKeys.ITEM_SCALE)(base, propertyMap)

// Mutable

fun HTPropertyMap.Mutable.addNamePattern(enPattern: String, jaPattern: String) {
    this.addNamePattern(HTLangPatternProvider.create(enPattern, jaPattern))
}

fun HTPropertyMap.Mutable.addNamePattern(value: HTLangPatternProvider) {
    this[HTTagPropertyKeys.LANG_PATTERN] = value
}
