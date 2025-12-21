package hiiragi283.core.api.material.prefix

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.tag.createTagKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

/**
 * 素材タグのプレフィックスを表すクラスです。
 * @param name プレフィックスのID
 * @param idPath 素材アイテムなどのIDのパターン
 * @param commonTagPath 共通タグのIDのパターン
 * @param tagPath 素材の共通タグのIDのパターン
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@JvmRecord
data class HTMaterialPrefix(
    val name: String,
    val idPath: String = "%s_$name",
    private val commonTagPath: String = "${HTConst.COMMON}:${name}s",
    private val tagPath: String = "$commonTagPath/%s",
) : HTPrefixLike {
    override fun asMaterialPrefix(): HTMaterialPrefix = this

    override fun createPath(material: HTMaterialLike): String = idPath.replace("%s", material.asMaterialName())

    override fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> = key.createTagKey(ResourceLocation.parse(commonTagPath))

    override fun <T : Any> createTagKey(key: RegistryKey<T>, name: String): TagKey<T> =
        key.createTagKey(ResourceLocation.parse(tagPath.replace("%s", name)))
}
