package hiiragi283.core.api.tag

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.RegistryKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * タグのプレフィックスを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
class HTTagPrefix(val rawCommonTag: RawTagKey, private val tagPattern: String) {
    constructor(commonTagId: String, tagPattern: String) : this(RawTagKey.common(commonTagId), tagPattern)

    /**
     * @since 0.15.3
     */
    fun materialTag(material: HTMaterialLike): RawTagKey = RawTagKey.common(tagPattern.replace("%s", material.asMaterialId().path))

    /**
     * 指定した[レジストリキー][key]と[素材][material]から，素材の共通タグを生成します。
     * @param T レジストリの要素のクラス
     */
    fun <T : Any> createTagKey(key: RegistryKey<T>, material: HTMaterialLike): TagKey<T> = materialTag(material).create(key)

    /**
     * 指定した[素材][material]から，[アイテム][Item]の素材の共通タグを生成します。
     */
    fun itemTagKey(material: HTMaterialLike): TagKey<Item> = createTagKey(Registries.ITEM, material)
}
