package hiiragi283.core.api.material.prefix

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.tag.createTagKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

/**
 * タグのプレフィックスを表すクラス
 *
 * [HTRegisterPrefixEvent]を通して登録する必要があります。
 */
@JvmRecord
data class HTMaterialPrefix(
    val name: String,
    private val commonTagPath: String = "${HTConst.COMMON}:${name}s",
    private val tagPath: String = "$commonTagPath/%s",
) : HTPrefixLike {
    override fun asMaterialPrefix(): HTMaterialPrefix = this

    override fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> = key.createTagKey(ResourceLocation.parse(commonTagPath))

    override fun <T : Any> createTagKey(key: RegistryKey<T>, name: String): TagKey<T> =
        key.createTagKey(ResourceLocation.parse(tagPath.replace("%s", name)))
}
