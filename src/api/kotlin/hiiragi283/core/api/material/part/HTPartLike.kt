package hiiragi283.core.api.material.part

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.RawTagKey
import net.minecraft.core.registries.Registries
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
     * 部品のキー
     * @since 21.1.1.0
     */
    val key: HTPartKey

    /**
     * [HTPart]に変換します。
     */
    fun asPart(): HTPart

    /**
     * 部品のキーの名前を返します。
     */
    fun asPartName(): String = key.name

    /**
     * 指定した[素材][key]から[ID][ResourceLocation]を生成します。
     */
    fun createId(key: HTMaterialKey): ResourceLocation
}

//    Extensions    //

/**
 * この[HTPartLike][this]から[プレフィックス][HTTagPrefix]を取得します。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
val HTPartLike.tagPrefix: HTTagPrefix? get() = this[HTPartPropertyKeys.TAG_PREFIX]

fun HTPartLike.materialTag(key: HTMaterialKey): RawTagKey? = this.tagPrefix?.materialTag(key)

fun HTPartLike.itemTagKey(key: HTMaterialKey): TagKey<Item>? = this.materialTag(key)?.create(Registries.ITEM)
