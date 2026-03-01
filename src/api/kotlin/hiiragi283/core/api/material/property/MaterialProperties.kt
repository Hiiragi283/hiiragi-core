package hiiragi283.core.api.material.property

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.item.tool.HTToolMaterial
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.computeIfAbsent
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.fluid.HTFluidTagPrefix
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import org.apache.commons.lang3.math.Fraction

fun HTPropertyMap.getDefaultPart(): HTDefaultPart? = this[HTMaterialPropertyKeys.DEFAULT_PART]

fun HTPropertyMap.getDefaultPart(material: HTMaterialLike): TagKey<Item>? = this.getDefaultPart()?.getTag(material.asMaterialKey())

fun HTPropertyMap.getDefaultScale(): Fraction = this.getOrDefault(HTMaterialPropertyKeys.DEFAULT_SCALE)

fun HTPropertyMap.getDefaultFluidAmount(): Int = this.getOrDefault(HTMaterialPropertyKeys.DEFAULT_FLUID_AMOUNT)

// Mutable

fun HTPropertyMap.Mutable.setDefaultPart(tagKey: TagKey<Item>, altItem: HTItemHolderLike<*>?) {
    this[HTMaterialPropertyKeys.DEFAULT_PART] = HTDefaultPart.BuiltIn(tagKey, altItem)
}

fun HTPropertyMap.Mutable.setDefaultPart(prefixed: HTDefaultPart.Prefixed) {
    this[HTMaterialPropertyKeys.DEFAULT_PART] = prefixed
}

fun HTPropertyMap.Mutable.addBlockPrefixes(vararg parts: HTPartLike) {
    this.computeIfAbsent(HTMaterialPropertyKeys.BLOCK_PREFIXES) { it.plus(parts) }
}

fun HTPropertyMap.Mutable.addBlockPrefixes(parts: Set<HTPartLike>) {
    this.computeIfAbsent(HTMaterialPropertyKeys.BLOCK_PREFIXES) { it.plus(parts) }
}

fun HTPropertyMap.Mutable.addFluidPrefixes(vararg tagPrefixes: HTFluidTagPrefix) {
    this.computeIfAbsent(HTMaterialPropertyKeys.FLUID_PREFIXES) { it.plus(tagPrefixes) }
}

fun HTPropertyMap.Mutable.addFluidPrefixes(tagPrefixes: Set<HTFluidTagPrefix>) {
    this.computeIfAbsent(HTMaterialPropertyKeys.FLUID_PREFIXES) { it.plus(tagPrefixes) }
}

fun HTPropertyMap.Mutable.addItemPrefixes(vararg parts: HTPartLike) {
    this.computeIfAbsent(HTMaterialPropertyKeys.ITEM_PREFIXES) { it.plus(parts) }
}

fun HTPropertyMap.Mutable.addItemPrefixes(parts: Set<HTPartLike>) {
    this.computeIfAbsent(HTMaterialPropertyKeys.ITEM_PREFIXES) { it.plus(parts) }
}

fun HTPropertyMap.Mutable.addToolPrefixes(material: HTToolMaterial, vararg toolTypes: HTToolType) {
    this[HTMaterialPropertyKeys.TOOL_MATERIAL] = material
    this.computeIfAbsent(HTMaterialPropertyKeys.TOOL_PREFIXES) { it.plus(toolTypes) }
}

fun HTPropertyMap.Mutable.addToolPrefixes(material: HTToolMaterial, toolTypes: Set<HTToolType>) {
    this[HTMaterialPropertyKeys.TOOL_MATERIAL] = material
    this.computeIfAbsent(HTMaterialPropertyKeys.TOOL_PREFIXES) { it.plus(toolTypes) }
}

fun HTPropertyMap.Mutable.setName(enName: String, jaName: String) {
    this.setName(HTLangName.create(enName, jaName))
}

fun HTPropertyMap.Mutable.setName(value: HTLangName) {
    this[HTMaterialPropertyKeys.LANG_NAME] = value
}

fun HTPropertyMap.Mutable.addCustomName(part: HTPartLike, enName: String, jaName: String) {
    this.addCustomName(part, HTLangName.create(enName, jaName))
}

fun HTPropertyMap.Mutable.addCustomName(part: HTPartLike, value: HTLangName) {
    this.computeIfAbsent(HTMaterialPropertyKeys.CUSTOM_LANG_NAME) { it.plus(part.asPart() to value) }
}

fun HTPropertyMap.Mutable.setTextureSet(name: String) {
    this.setTextureSet(HTMaterialTextureSet(name, HTMaterialTextureSet.DEFAULT))
}

fun HTPropertyMap.Mutable.setTextureSet(name: String, parent: HTMaterialTextureSet) {
    this.setTextureSet(HTMaterialTextureSet(name, parent))
}

fun HTPropertyMap.Mutable.setTextureSet(textureSet: HTMaterialTextureSet) {
    this[HTMaterialPropertyKeys.TEXTURE_SET] = textureSet
}
