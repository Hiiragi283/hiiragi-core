package hiiragi283.core.api.tag

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import java.util.TreeMap

/**
 * タグのプレフィックスを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
class HTTagPrefix private constructor(
    val name: String,
    private val commonTagId: ResourceLocation,
    private val tagPattern: String,
    properties: HTPropertyMap,
) : Comparable<HTTagPrefix>,
    HTPropertyMap by properties {
    companion object {
        @JvmStatic
        val instances: Map<String, HTTagPrefix> get() = _instances

        @JvmStatic
        private val _instances: MutableMap<String, HTTagPrefix> = TreeMap()

        @HTBuilderMarker
        @JvmStatic
        inline fun create(
            name: String,
            commonTagPattern: String,
            tagPattern: String,
            builderAction: HTPropertyMap.Mutable.() -> Unit,
        ): HTTagPrefix = Builder(name).apply(builderAction).build(HTConst.COMMON.toId(commonTagPattern), tagPattern)

        @HTBuilderMarker
        @JvmStatic
        inline fun create(
            name: String,
            commonTagId: ResourceLocation,
            tagPattern: String,
            builderAction: HTPropertyMap.Mutable.() -> Unit,
        ): HTTagPrefix = Builder(name).apply(builderAction).build(commonTagId, tagPattern)
    }

    init {
        require(_instances.put(name, this) == null) { "Duplicated tag prefix: $name" }
    }

    /**
     * 指定した[素材][material]から素材アイテムなどの[ID][ResourceLocation]を生成します。
     */
    fun createId(material: HTMaterialLike): ResourceLocation {
        val pathPattern: String = this[HTTagPropertyKeys.ID_PATTERN] ?: "%s_$name"
        return material.asMaterialId().withPath { pathPattern.replace("%s", it) }
    }

    fun <T : Any> createKey(key: RegistryKey<T>, material: HTMaterialLike): ResourceKey<T> = createId(material).let(key::createKey)

    fun itemKey(material: HTMaterialLike): ResourceKey<Item> = createKey(Registries.ITEM, material)

    /**
     * 指定した[レジストリキー][key]から，共通タグを生成します。
     * @param T レジストリの要素のクラス
     */
    fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> = key.createTagKey(commonTagId)

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
        fun build(commonTagId: ResourceLocation, tagPattern: String): HTTagPrefix =
            HTTagPrefix(name.lowercase(), commonTagId, tagPattern, this)
    }
}
