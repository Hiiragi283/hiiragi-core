package hiiragi283.core.api.tag

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
class HTTagPrefix private constructor(val name: String, properties: HTPropertyMap) :
    Comparable<HTTagPrefix>,
    HTPropertyMap by properties {
        companion object {
            @JvmStatic
            inline fun create(name: String, builderAction: HTPropertyMap.Mutable.() -> Unit): HTTagPrefix =
                Builder(name).apply(builderAction).build()
        }

        /**
         * 指定した[素材][material]から素材アイテムなどの[ID][ResourceLocation]を生成します。
         */
        fun createId(material: HTMaterialLike): ResourceLocation {
            val pathPattern: String = this[HTTagPropertyKeys.ID_PATTERN] ?: "%s_$name"
            val materialId: ResourceLocation = material.asMaterialId()
            return materialId.namespace.toId(pathPattern.replace("%s", materialId.path))
        }

        /**
         * 指定した[レジストリキー][key]から，共通タグを生成します。
         * @param T レジストリの要素のクラス
         */
        fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> {
            val tagPath: String = this[HTTagPropertyKeys.COMMON_TAG_PATTERN] ?: "${name}s"
            val id: ResourceLocation = HTConst.COMMON.toId(tagPath)
            return key.createTagKey(id)
        }

        /**
         * 指定した[レジストリキー][key]と[素材][material]から，素材の共通タグを生成します。
         * @param T レジストリの要素のクラス
         */
        fun <T : Any> createTagKey(key: RegistryKey<T>, material: HTMaterialLike): TagKey<T> {
            val tagPathPattern: String = this[HTTagPropertyKeys.TAG_PATTERN] ?: "${name}s/%s"
            val id: ResourceLocation = HTConst.COMMON.toId(tagPathPattern.replace("%s", material.asMaterialId().path))
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
            fun build(): HTTagPrefix = HTTagPrefix(name.lowercase(), this)
        }
    }
