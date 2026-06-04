package hiiragi283.lib.tag

import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.registry.RegistryKey
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
     * @since 0.16.0
     */
    fun materialTag(material: HTMaterialKey): RawTagKey = RawTagKey.common(tagPattern.replace("%s", material.identifier().path))

    /**
     * 指定した[レジストリキー][key]と[素材][material]から，素材の共通タグを生成します。
     * @param T レジストリの要素のクラス
     */
    fun <T : Any> createTagKey(key: RegistryKey<T>, material: HTMaterialKey): TagKey<T> = materialTag(material).create(key)

    /**
     * 指定した[素材][material]から，[アイテム][Item]の素材の共通タグを生成します。
     */
    fun itemTagKey(material: HTMaterialKey): TagKey<Item> = createTagKey(Registries.ITEM, material)
}
