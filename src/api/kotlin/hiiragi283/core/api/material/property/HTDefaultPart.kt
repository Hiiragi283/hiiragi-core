package hiiragi283.core.api.material.property

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * 素材のデフォルトのアイテムを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
sealed interface HTDefaultPart {
    /**
     * 指定した[key]から素材アイテムのタグを取得します。
     */
    fun getTag(key: HTMaterialKey): TagKey<Item>

    /**
     * 指定した[key]から素材アイテムを取得します。
     * @return 対応するアイテムがない場合は`null`
     */
    fun getItem(key: HTMaterialKey): HTItemHolderLike<*>?

    /**
     * レシピの生成時に使用されるサフィックスを取得します。
     */
    fun getSuffix(): String

    /**
     * 既存の[TagKey]と[Item]に基づいた[HTDefaultPart]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    data class Tag(val tagKey: TagKey<Item>, val item: HTItemHolderLike<*>) : HTDefaultPart {
        override fun getTag(key: HTMaterialKey): TagKey<Item> = tagKey

        override fun getItem(key: HTMaterialKey): HTItemHolderLike<*> = item

        override fun getSuffix(): String = tagKey.location().path
    }

    /**
     * [HTTagPrefix]に基づいた[HTDefaultPart]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    enum class Prefixed : HTDefaultPart {
        CROP,
        DUST,
        FUEL,
        GEM,
        INGOT,
        PEARL,
        PLATE,
        ;

        val prefix: HTTagPrefix get() = when (this) {
            CROP -> CommonTagPrefixes.CROP
            DUST -> CommonTagPrefixes.DUST
            FUEL -> CommonTagPrefixes.FUEL
            GEM -> CommonTagPrefixes.GEM
            INGOT -> CommonTagPrefixes.INGOT
            PEARL -> CommonTagPrefixes.PEARL
            PLATE -> CommonTagPrefixes.PLATE
        }

        override fun getTag(key: HTMaterialKey): TagKey<Item> = prefix.itemTagKey(key)

        override fun getItem(key: HTMaterialKey): HTItemHolderLike<*>? = with(HiiragiCoreAccess.INSTANCE) {
            when {
                prefix.contains(HTTagPropertyKeys.BLOCK_PROP) -> getBlockOrVanilla(prefix, key)
                else -> getItemOrVanilla(prefix, key)
            }
        }

        override fun getSuffix(): String = prefix.name
    }
}
