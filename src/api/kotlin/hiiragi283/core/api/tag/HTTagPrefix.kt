package hiiragi283.core.api.tag

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.toId
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * タグのプレフィックスを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
class HTTagPrefix(private val commonTagId: Identifier, private val tagPattern: String) {
    constructor(commonTagId: String, tagPattern: String) : this(HTConst.COMMON.toId(commonTagId), tagPattern)

    /**
     * 指定した[レジストリキー][key]から，共通タグを生成します。
     * @param T レジストリの要素のクラス
     */
    fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> = key.createTagKey(commonTagId)

    /**
     * 指定した[レジストリキー][key]と[素材][material]から，共通タグの一覧を生成します。
     * @param T レジストリの要素のクラス
     */
    fun <T : Any> createTagKeys(key: RegistryKey<T>, material: HTMaterialLike): Set<TagKey<T>> = material
        .asMaterial()
        .tagName
        .map { tagPattern.replace("%s", it) }
        .map(key::createCommonTag)
        .toSet()

    /**
     * 指定した[素材][material]から，[アイテム][Item]の共通タグの一覧を生成します。
     */
    fun itemTagKeys(material: HTMaterialLike): Set<TagKey<Item>> = createTagKeys(Registries.ITEM, material)
}
