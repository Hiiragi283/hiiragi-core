package hiiragi283.core.api.tag

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.toId
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

/**
 * ジェネリクスのない[TagKey]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
@JvmInline
value class RawTagKey private constructor(val location: ResourceLocation) {
    companion object {
        @JvmStatic
        fun common(path: String): RawTagKey = create(HTConst.COMMON.toId(path))

        @JvmStatic
        fun common(vararg path: String): RawTagKey = create(HTConst.COMMON.toId(*path))

        @JvmStatic
        fun copy(parent: TagKey<*>): RawTagKey = create(parent.location())

        @JvmStatic
        fun create(location: ResourceLocation): RawTagKey = RawTagKey(location)
    }

    fun <T : Any> create(key: RegistryKey<T>): TagKey<T> = TagKey.create(key, location)
}
