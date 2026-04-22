package hiiragi283.core.api.material.part

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.RawTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * 部品を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
interface HTPartLike : HTPropertyGetter {
    /**
     * [HTPart]に変換します。
     */
    fun asPart(): HTPart

    /**
     * [asPart]から部品の名前を取得します。
     */
    fun asPartName(): String

    /**
     * 指定した[素材][material]から[ID][ResourceLocation]を生成します。
     */
    fun createId(material: HTMaterialLike): ResourceLocation

    /**
     * 指定した[素材][material]から[ResourceKey]を生成します。
     */
    fun <T : Any> createKey(key: RegistryKey<T>, material: HTMaterialLike): ResourceKey<T> = createId(material).let(key::createKey)
}

//    Extensions    //

/**
 * この[HTPartLike][this]から[プレフィックス][HTTagPrefix]を取得します。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
val HTPartLike.tagPrefix: HTTagPrefix? get() = this[HTPartPropertyKeys.TAG_PREFIX]

fun HTPartLike.materialTag(material: HTMaterialLike): RawTagKey? = this.tagPrefix?.materialTag(material)

fun HTPartLike.itemTagKey(material: HTMaterialLike): TagKey<Item>? = this.materialTag(material)?.create(Registries.ITEM)
