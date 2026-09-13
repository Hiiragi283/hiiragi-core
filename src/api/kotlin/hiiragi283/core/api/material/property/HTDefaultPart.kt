package hiiragi283.core.api.material.property

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPartKey
import hiiragi283.core.api.resource.SimpleSupplierWithKey
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
     * 指定した[素材][key]から素材アイテムのタグを取得します。
     */
    fun getTag(key: HTMaterialKey): TagKey<Item>

    /**
     * 指定した[素材][key]から素材アイテムを取得します。
     * @return 対応するアイテムがない場合は`null`
     */
    fun getItem(key: HTMaterialKey): HTMaterialContents.ItemEntry?

    /**
     * レシピの生成時に使用されるサフィックスを取得します。
     */
    fun getSuffix(): String

    /**
     * 既存の[TagKey]と[Item]に基づいた[HTDefaultPart]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    @JvmRecord
    data class BuiltIn(val tagKey: TagKey<Item>, val item: SimpleSupplierWithKey<Item>?) : HTDefaultPart {
        override fun getTag(key: HTMaterialKey): TagKey<Item> = tagKey

        override fun getItem(key: HTMaterialKey): HTMaterialContents.ItemEntry? = this.item?.let { HTMaterialContents.ItemEntry(it, true) }

        override fun getSuffix(): String = tagKey.location().path
    }

    /**
     * [HTPartKey]と[HTTagPrefix]に基づいた[HTDefaultPart]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    enum class Prefixed : HTDefaultPart {
        FUEL,
        GEM,
        INGOT,
        PEARL,
        ;

        val part: HTPartKey get() = when (this) {
            FUEL -> CommonParts.FUEL
            GEM -> CommonParts.GEM
            INGOT -> CommonParts.INGOT
            PEARL -> CommonParts.PEARL
        }
        val prefix: HTTagPrefix get() = when (this) {
            // CROP -> CommonTagPrefixes.CROP
            // DUST -> CommonTagPrefixes.DUST
            FUEL -> CommonTagPrefixes.FUEL
            GEM -> CommonTagPrefixes.GEM
            INGOT -> CommonTagPrefixes.INGOT
            PEARL -> CommonTagPrefixes.PEARL
        }

        override fun getTag(key: HTMaterialKey): TagKey<Item> = prefix.itemTagKey(key)

        override fun getItem(key: HTMaterialKey): HTMaterialContents.ItemEntry? = HiiragiCoreAccess.INSTANCE.getMaterialBlockOrItem(part, key)

        override fun getSuffix(): String = part.name
    }
}
