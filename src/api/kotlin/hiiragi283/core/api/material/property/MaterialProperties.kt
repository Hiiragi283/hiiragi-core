package hiiragi283.core.api.material.property

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.computeIfAbsent
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import org.apache.commons.lang3.math.Fraction

fun HTMaterial.getDefaultPart(): HTDefaultPart? = this[HTMaterialPropertyKeys.DEFAULT_PART]

fun HTMaterial.getDefaultPart(key: HTMaterialKey): TagKey<Item>? = this.getDefaultPart()?.getTag(key)

/**
 * @since 0.12.0
 */
fun HTMaterial.getDefaultScale(): Fraction = this.getOrDefault(HTMaterialPropertyKeys.DEFAULT_SCALE)

// Mutable

fun HTPropertyMap.Builder.setDefaultPart(tagKey: TagKey<Item>, altItem: SimpleSupplierWithKey<Item>?) {
    this[HTMaterialPropertyKeys.DEFAULT_PART] = HTDefaultPart.BuiltIn(tagKey, altItem)
}

fun HTPropertyMap.Builder.setDefaultPart(prefixed: HTDefaultPart.Prefixed) {
    this[HTMaterialPropertyKeys.DEFAULT_PART] = prefixed
}

fun HTPropertyMap.Builder.addBlockPrefixes(vararg parts: HTPartLike) {
    this.computeIfAbsent(HTMaterialPropertyKeys.BLOCK_PREFIXES) { it.plus(parts) }
}

fun HTPropertyMap.Builder.addBlockPrefixes(parts: Set<HTPartLike>) {
    this.computeIfAbsent(HTMaterialPropertyKeys.BLOCK_PREFIXES) { it.plus(parts) }
}

fun HTPropertyMap.Builder.addItemPrefixes(vararg parts: HTPartLike) {
    this.computeIfAbsent(HTMaterialPropertyKeys.ITEM_PREFIXES) { it.plus(parts) }
}

fun HTPropertyMap.Builder.addItemPrefixes(parts: Set<HTPartLike>) {
    this.computeIfAbsent(HTMaterialPropertyKeys.ITEM_PREFIXES) { it.plus(parts) }
}

fun HTPropertyMap.Builder.addToolPrefixes(material: HTToolMaterial, vararg toolTypes: HTToolType) {
    this[HTMaterialPropertyKeys.TOOL_MATERIAL] = material
    this.computeIfAbsent(HTMaterialPropertyKeys.TOOL_PREFIXES) { it.plus(toolTypes) }
}

fun HTPropertyMap.Builder.addToolPrefixes(material: HTToolMaterial, toolTypes: Set<HTToolType>) {
    this[HTMaterialPropertyKeys.TOOL_MATERIAL] = material
    this.computeIfAbsent(HTMaterialPropertyKeys.TOOL_PREFIXES) { it.plus(toolTypes) }
}

fun HTPropertyMap.Builder.setName(enName: String, jaName: String) {
    this.setName(HTLangName(enName, jaName))
}

fun HTPropertyMap.Builder.setName(value: HTLangName) {
    this[HTMaterialPropertyKeys.LANG_NAME] = value
}

fun HTPropertyMap.Builder.addCustomName(part: HTPartLike, enName: String, jaName: String) {
    this.addCustomName(part, HTLangName(enName, jaName))
}

fun HTPropertyMap.Builder.addCustomName(part: HTPartLike, value: HTLangName) {
    this.computeIfAbsent(HTMaterialPropertyKeys.CUSTOM_LANG_NAME) { it.plus(part.asPart() to value) }
}

fun HTPropertyMap.Builder.setTextureSet(name: String) {
    this.setTextureSet(HTMaterialTextureSet(name, HTMaterialTextureSet.DEFAULT))
}

fun HTPropertyMap.Builder.setTextureSet(name: String, parent: HTMaterialTextureSet) {
    this.setTextureSet(HTMaterialTextureSet(name, parent))
}

fun HTPropertyMap.Builder.setTextureSet(textureSet: HTMaterialTextureSet) {
    this[HTMaterialPropertyKeys.TEXTURE_SET] = textureSet
}
