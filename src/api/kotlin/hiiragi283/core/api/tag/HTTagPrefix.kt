package hiiragi283.core.api.tag

import hiiragi283.core.api.material.HTMaterialKey
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
     * @since 0.16.0
     */
    fun materialTag(key: HTMaterialKey): RawTagKey = RawTagKey.common(tagPattern.replace("%s", key.name))

    /**
     * 指定した[レジストリキー][registryKey]と[素材][key]から，素材の共通タグを生成します。
     * @param T レジストリの要素のクラス
     */
    fun <T : Any> createTagKey(registryKey: RegistryKey<T>, key: HTMaterialKey): TagKey<T> = materialTag(key).create(registryKey)

    /**
     * 指定した[素材][key]から，[アイテム][Item]の素材の共通タグを生成します。
     */
    fun itemTagKey(key: HTMaterialKey): TagKey<Item> = createTagKey(Registries.ITEM, key)
}
