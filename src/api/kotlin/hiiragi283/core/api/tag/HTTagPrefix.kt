package hiiragi283.core.api.tag

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.toId
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

class HTTagPrefix(private val commonTagId: ResourceLocation, private val tagPattern: String) {
    constructor(commonTagId: String, tagPattern: String) : this(HTConst.COMMON.toId(commonTagId), tagPattern)

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
}
