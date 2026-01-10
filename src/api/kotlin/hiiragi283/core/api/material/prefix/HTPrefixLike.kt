package hiiragi283.core.api.material.prefix

import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.RegistryKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * [HTMaterialPrefix]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTPrefixLike {
    /**
     * 保持している[プレフィックス][HTMaterialPrefix]を返します。
     */
    fun asMaterialPrefix(): HTMaterialPrefix

    /**
     * 保持している[プレフィックス][HTMaterialPrefix]の名前を返します。
     */
    fun asPrefixName(): String = asMaterialPrefix().name

    /**
     * 指定した[ほかの素材][other]とプレフィックスが一致するか判定します。
     */
    fun isOf(other: HTPrefixLike): Boolean = this.asMaterialPrefix() == other.asMaterialPrefix()

    /**
     * 指定した[素材][material]から素材アイテムなどのIDのパスを生成します。
     */
    fun createPath(material: HTMaterialLike): String = asMaterialPrefix().createPath(material)

    /**
     * 指定した[レジストリキー][key]から，共通タグを生成します。
     * @param T レジストリの要素のクラス
     */
    fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> = asMaterialPrefix().createCommonTagKey(key)

    /**
     * 指定した[レジストリキー][key]と[素材][material]から，素材の共通タグを生成します。
     * @param T レジストリの要素のクラス
     */
    fun <T : Any> createTagKey(key: RegistryKey<T>, material: HTMaterialLike): TagKey<T> = createTagKey(key, material.asMaterialName())

    /**
     * 指定した[レジストリキー][key]と[素材名][name]から，素材の共通タグを生成します。
     * @param T レジストリの要素のクラス
     */
    fun <T : Any> createTagKey(key: RegistryKey<T>, name: String): TagKey<T> = asMaterialPrefix().createTagKey(key, name)

    /**
     * 指定した[素材][material]から，[アイテム][Item]の素材の共通タグを生成します。
     */
    fun itemTagKey(material: HTMaterialLike): TagKey<Item> = createTagKey(Registries.ITEM, material)
}
