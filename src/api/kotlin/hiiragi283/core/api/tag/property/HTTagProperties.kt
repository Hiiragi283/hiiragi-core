package hiiragi283.core.api.tag.property

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.property.HTPropertyMap

// Mutable

fun HTPropertyMap.Mutable.addNamePattern(enPattern: String, jaPattern: String) {
    this.addNamePattern(HTLangPatternProvider.create(enPattern, jaPattern))
}

fun HTPropertyMap.Mutable.addNamePattern(value: HTLangPatternProvider) {
    this[HTTagPropertyKeys.LANG_PATTERN] = value
}
