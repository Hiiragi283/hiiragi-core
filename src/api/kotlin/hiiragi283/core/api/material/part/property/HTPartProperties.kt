package hiiragi283.core.api.material.part.property

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.toFraction
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import org.apache.commons.lang3.math.Fraction

/**
 * この[HTPart][this]から[プレフィックス][HTTagPrefix]を取得します。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
val HTPart.tagPrefix: HTTagPrefix? get() = this[HTPartPropertyKeys.TAG_PREFIX]

fun HTPart.itemTagKey(key: HTMaterialKey): TagKey<Item>? = this.tagPrefix?.itemTagKey(key)

/**
 * @since 0.8.0
 */
fun HTPart.getScaledAmount(base: Int, material: HTMaterial): Fraction = this.getScaledAmount(base.toFraction(1), material)

/**
 * @since 0.8.0
 */
fun HTPart.getScaledAmount(base: Float, material: HTMaterial): Fraction = this.getScaledAmount(base.toFraction(), material)

/**
 * @since 0.8.0
 */
fun HTPart.getScaledAmount(base: Fraction, material: HTMaterial): Fraction = this.getOrDefault(HTPartPropertyKeys.ITEM_SCALE)(base, material)

// Mutable

fun HTPropertyMap.Builder.addNamePattern(enPattern: String, jaPattern: String) {
    this.addNamePattern(HTLangPatternProvider(enPattern, jaPattern))
}

fun HTPropertyMap.Builder.addNamePattern(value: HTLangPatternProvider) {
    this[HTPartPropertyKeys.LANG_PATTERN] = value
}
