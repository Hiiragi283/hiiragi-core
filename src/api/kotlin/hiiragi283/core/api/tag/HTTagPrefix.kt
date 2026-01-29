package hiiragi283.core.api.tag

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * タグのプレフィックスを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
class HTTagPrefix private constructor(
    val name: String,
    private val commonTagPattern: String,
    private val tagPattern: String,
    properties: HTPropertyMap,
) : Comparable<HTTagPrefix>,
    HTPropertyMap by properties {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(
            name: String,
            commonTagPattern: String,
            tagPattern: String,
            builderAction: HTPropertyMap.Mutable.() -> Unit,
        ): HTTagPrefix = Builder(name).apply(builderAction).build(commonTagPattern, tagPattern)
    }

    /**
     * 指定した[素材][material]から素材アイテムなどの[ID][ResourceLocation]を生成します。
     */
    fun createId(material: HTMaterialLike): ResourceLocation {
        val pathPattern: String = this[HTTagPropertyKeys.ID_PATTERN] ?: "%s_$name"
        return material.asMaterialId().withPath { pathPattern.replace("%s", it) }
    }

    /**
     * 指定した[レジストリキー][key]から，共通タグを生成します。
     * @param T レジストリの要素のクラス
     */
    fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> {
        val id: ResourceLocation = HTConst.COMMON.toId(commonTagPattern)
        return key.createTagKey(id)
    }

    /**
     * 指定した[レジストリキー][key]と[素材][material]から，素材の共通タグを生成します。
     * @param T レジストリの要素のクラス
     */
    fun <T : Any> createTagKey(key: RegistryKey<T>, material: HTMaterialLike): TagKey<T> {
        val id: ResourceLocation = HTConst.COMMON.toId(tagPattern.replace("%s", material.asMaterialId().path))
        return key.createTagKey(id)
    }

    /**
     * 指定した[素材][material]から，[アイテム][Item]の素材の共通タグを生成します。
     */
    fun itemTagKey(material: HTMaterialLike): TagKey<Item> = createTagKey(Registries.ITEM, material)

    override fun compareTo(other: HTTagPrefix): Int = this.name.compareTo(other.name)

    //    Builder    //

    /**
     * @author Hiiragi Tsubasa
     * @since 0.7.0
     */
    class Builder(private val name: String) : HTPropertyMap.Mutable by HTBasicPropertyMap.Mutable() {
        fun build(commonTagPattern: String, tagPattern: String): HTTagPrefix =
            HTTagPrefix(name.lowercase(), commonTagPattern, tagPattern, this)
    }
}
