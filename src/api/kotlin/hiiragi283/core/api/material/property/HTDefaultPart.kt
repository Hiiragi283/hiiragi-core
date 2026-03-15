package hiiragi283.core.api.material.property

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * 素材のデフォルトのアイテムを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
sealed interface HTDefaultPart {
    /**
     * 指定した[素材][material]から素材アイテムのタグを取得します。
     */
    fun getTag(material: HTMaterialLike): TagKey<Item>

    /**
     * 指定した[素材][material]から素材アイテムを取得します。
     * @return 対応するアイテムがない場合は`null`
     */
    fun getItem(material: HTMaterialLike): HTMaterialContents.ItemEntry?

    /**
     * レシピの生成時に使用されるサフィックスを取得します。
     */
    fun getSuffix(): String

    /**
     * 既存の[TagKey]と[Item]に基づいた[HTDefaultPart]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    data class BuiltIn(val tagKey: TagKey<Item>, val item: HTItemHolderLike<*>?) : HTDefaultPart {
        override fun getTag(material: HTMaterialLike): TagKey<Item> = tagKey

        override fun getItem(material: HTMaterialLike): HTMaterialContents.ItemEntry? =
            this.item?.let { HTMaterialContents.ItemEntry(it, true) }

        override fun getSuffix(): String = tagKey.location().path
    }

    /**
     * [HTPart]と[HTTagPrefix]に基づいた[HTDefaultPart]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    enum class Prefixed : HTDefaultPart {
        FUEL,
        GEM,
        INGOT,
        PEARL,
        ;

        val part: HTPart get() = when (this) {
            FUEL -> CommonParts.FUEL
            GEM -> CommonParts.GEM
            INGOT -> CommonParts.INGOT
            PEARL -> CommonParts.PEARL
        }.asPart()
        val prefix: HTTagPrefix get() = when (this) {
            // CROP -> CommonTagPrefixes.CROP
            // DUST -> CommonTagPrefixes.DUST
            FUEL -> CommonTagPrefixes.FUEL
            GEM -> CommonTagPrefixes.GEM
            INGOT -> CommonTagPrefixes.INGOT
            PEARL -> CommonTagPrefixes.PEARL
        }

        override fun getTag(material: HTMaterialLike): TagKey<Item> = prefix.itemTagKey(material)

        override fun getItem(material: HTMaterialLike): HTMaterialContents.ItemEntry? =
            HiiragiCoreAccess.INSTANCE.getMaterialBlockOrItem(part, material)

        override fun getSuffix(): String = part.name
    }
}
