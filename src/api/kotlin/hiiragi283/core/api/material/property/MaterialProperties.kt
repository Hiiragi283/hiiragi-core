package hiiragi283.core.api.material.property

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.HTPropertyMap

fun HTPropertyMap.getDefaultPart(): HTMaterialPrefix? = this[HTMaterialPropertyKeys.DEFAULT_PART]

fun HTPropertyMap.getDefaultFluidAmount(): Int = this.getOrDefault(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT)

fun HTPropertyMap.getStorageBlock(): HTStorageBlockProperty = this.getOrDefault(HTMaterialPropertyKeys.STORAGE_BLOCK)

// Mutable

fun <K : Any, V : Any> HTPropertyMap.Mutable.computeMap(propertyKey: HTPropertyKey<Map<K, V>>, pair: Pair<K, V>) {
    val newMap: Map<K, V> = this[propertyKey]?.plus(pair) ?: mapOf(pair)
    this[propertyKey] = newMap
}

fun <K : Any, V : Any> HTPropertyMap.Mutable.computeMap(propertyKey: HTPropertyKey<Map<K, V>>, vararg pairs: Pair<K, V>) {
    val newMap: Map<K, V> = this[propertyKey]?.plus(pairs) ?: mapOf(*pairs)
    this[propertyKey] = newMap
}

fun HTPropertyMap.Mutable.addDefaultPart(prefix: HTPrefixLike) {
    this[HTMaterialPropertyKeys.DEFAULT_PART] = prefix.asMaterialPrefix()
}

private fun createLangName(enName: String, jaName: String): HTLangName = HTLangName { type: HTLanguageType ->
    when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jaName
    }
}

fun HTPropertyMap.Mutable.addName(enName: String, jaName: String) {
    this.addName(createLangName(enName, jaName))
}

fun HTPropertyMap.Mutable.addName(value: HTLangName) {
    this[HTMaterialPropertyKeys.LANG_NAME] = value
}

fun HTPropertyMap.Mutable.addCustomName(prefix: HTPrefixLike, enName: String, jaName: String) {
    this.addCustomName(prefix, createLangName(enName, jaName))
}

fun HTPropertyMap.Mutable.addCustomName(prefix: HTPrefixLike, value: HTLangName) {
    this.computeMap(HTMaterialPropertyKeys.CUSTOM_LANG_NAME, prefix.asMaterialPrefix() to value)
}

fun HTPropertyMap.Mutable.addTextureSet(name: String, parent: HTMaterialTextureSet = HTMaterialTextureSet.DEFAULT) {
    this.addTextureSet(HTMaterialTextureSet(name, parent))
}

fun HTPropertyMap.Mutable.addTextureSet(textureSet: HTMaterialTextureSet) {
    this[HTMaterialPropertyKeys.TEXTURE_SET] = textureSet
}
