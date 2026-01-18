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

class HTTagPrefix private constructor(val name: String, properties: HTPropertyMap) : HTPropertyMap by properties {
    companion object {
        @JvmStatic
        inline fun create(name: String, builderAction: HTPropertyMap.Mutable.() -> Unit): HTTagPrefix =
            Builder(name).apply(builderAction).build()
    }

    fun createPath(material: HTMaterialLike): String {
        val pathPattern: String = this[HTTagPropertyKeys.ID_PATTERN] ?: "%s_$name"
        return pathPattern.replace("%s", material.asMaterialName())
    }

    fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> {
        val tagPath: String = this[HTTagPropertyKeys.COMMON_TAG_PATTERN] ?: "${name}s"
        val id: ResourceLocation = HTConst.COMMON.toId(tagPath)
        return key.createTagKey(id)
    }

    fun <T : Any> createTagKey(key: RegistryKey<T>, material: HTMaterialLike): TagKey<T> {
        val tagPathPattern: String = this[HTTagPropertyKeys.TAG_PATTERN] ?: "${name}s/%s"
        val id: ResourceLocation = HTConst.COMMON.toId(tagPathPattern.replace("%s", material.asMaterialName()))
        return key.createTagKey(id)
    }

    fun itemTagKey(material: HTMaterialLike): TagKey<Item> = createTagKey(Registries.ITEM, material)

    //    Builder    //

    class Builder(private val name: String) : HTPropertyMap.Mutable by HTBasicPropertyMap.Mutable() {
        fun build(): HTTagPrefix = HTTagPrefix(name.lowercase(), this)
    }
}
