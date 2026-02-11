package hiiragi283.core.api.material.property

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * 素材システムに基づいた部品を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
interface HTMaterialPart {
    /**
     * 素材アイテムのタグを取得します。
     */
    val tagKey: TagKey<Item>

    /**
     * 対応する素材アイテムを取得します。
     * @return 対応するアイテムがない場合は`null`
     */
    val item: HTItemHolderLike<*>?

    /**
     * レシピの生成時に使用されるサフィックスを取得します。
     */
    val suffix: String

    /**
     * 既存の[TagKey]と[Item]に基づいた[HTMaterialPart]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.9.0
     */
    data class BuiltIn(override val tagKey: TagKey<Item>, override val item: HTItemHolderLike<*>) : HTMaterialPart {
        override val suffix: String get() = tagKey.location().path
    }

    /**
     * [HTTagPrefix]と[HTMaterialKey]に基づいた[HTMaterialPart]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.9.0
     */
    data class Deferred(val prefix: HTTagPrefix, val key: HTMaterialKey) : HTMaterialPart {
        override val tagKey: TagKey<Item>
            get() = prefix.itemTagKey(key)
        override val item: HTItemHolderLike<*>?
            get() = with(HiiragiCoreAccess.INSTANCE.patchedMaterialContents) {
                getBlock(prefix, key) ?: getItem(prefix, key)
            }
        override val suffix: String = prefix.name
    }
}
