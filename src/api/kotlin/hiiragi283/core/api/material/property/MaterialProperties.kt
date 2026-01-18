package hiiragi283.core.api.material.property

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

fun HTPropertyMap.getDefaultPart(): HTDefaultPart? = this[HTMaterialPropertyKeys.DEFAULT_PART]

fun HTPropertyMap.getDefaultPart(material: HTMaterialLike): TagKey<Item>? = this.getDefaultPart()?.getTag(material.asMaterialKey())

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

fun HTPropertyMap.Mutable.addDefaultPart(tagKey: TagKey<Item>, altItem: HTItemHolderLike<*>) {
    this[HTMaterialPropertyKeys.DEFAULT_PART] = HTDefaultPart.Tag(tagKey, altItem)
}

fun HTPropertyMap.Mutable.addDefaultPart(prefix: HTTagPrefix) {
    this[HTMaterialPropertyKeys.DEFAULT_PART] = HTDefaultPart.Material(prefix)
}

fun HTPropertyMap.Mutable.setBlockPrefixes(vararg tagPrefixes: HTTagPrefix) {
    this.setBlockPrefixes(setOf(*tagPrefixes))
}

fun HTPropertyMap.Mutable.setBlockPrefixes(tagPrefixes: Set<HTTagPrefix>) {
    this[HTMaterialPropertyKeys.BLOCK_PREFIXES] = tagPrefixes
}

fun HTPropertyMap.Mutable.setItemPrefixes(vararg tagPrefixes: HTTagPrefix) {
    this.setItemPrefixes(setOf(*tagPrefixes))
}

fun HTPropertyMap.Mutable.setItemPrefixes(tagPrefixes: Set<HTTagPrefix>) {
    this[HTMaterialPropertyKeys.ITEM_PREFIXES] = tagPrefixes
}

fun HTPropertyMap.Mutable.addName(enName: String, jaName: String) {
    this.addName(HTLangName.create(enName, jaName))
}

fun HTPropertyMap.Mutable.addName(value: HTLangName) {
    this[HTMaterialPropertyKeys.LANG_NAME] = value
}

fun HTPropertyMap.Mutable.addCustomName(prefix: HTTagPrefix, enName: String, jaName: String) {
    this.addCustomName(prefix, HTLangName.create(enName, jaName))
}

fun HTPropertyMap.Mutable.addCustomName(prefix: HTTagPrefix, value: HTLangName) {
    this.computeMap(HTMaterialPropertyKeys.CUSTOM_LANG_NAME, prefix to value)
}

fun HTPropertyMap.Mutable.addTextureSet(name: String, parent: HTMaterialTextureSet = HTMaterialTextureSet.DEFAULT) {
    this.addTextureSet(HTMaterialTextureSet(name, parent))
}

fun HTPropertyMap.Mutable.addTextureSet(textureSet: HTMaterialTextureSet) {
    this[HTMaterialPropertyKeys.TEXTURE_SET] = textureSet
}
