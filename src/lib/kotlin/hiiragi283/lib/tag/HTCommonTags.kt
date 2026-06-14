package hiiragi283.lib.tag

import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.resource.toId
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * 共通の[TagKey]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTCommonTags {
    data object Blocks {
        @JvmStatic
        private fun common(vararg path: String): TagKey<Block> = BlockTags.create(HTConstants.COMMON.toId(*path))
    }

    data object Items {
        // Pearls
        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = ItemTags.create(HTConstants.COMMON.toId(*path))
    }

    data object MaterialContents {
        // Elements
        @JvmField
        val ELEMENTS: TagKey<HTMaterialContents> = common(HTConstants.ELEMENTS)

        @JvmField
        val ELEMENTS_METAL: TagKey<HTMaterialContents> = common(HTConstants.ELEMENTS, "metal")

        @JvmField
        val ELEMENTS_ALKALI_METAL: TagKey<HTMaterialContents> = common(HTConstants.ELEMENTS, "alkali_metal")

        @JvmField
        val ELEMENTS_ALKALI_EARTH_METAL: TagKey<HTMaterialContents> = common(HTConstants.ELEMENTS, "alkali_earth_metal")

        /**
         * @since 26.1.1
         */
        @JvmField
        val ELEMENTS_PLATINUM_GROUP: TagKey<HTMaterialContents> = common(HTConstants.ELEMENTS, "platinum_group")

        // Minerals
        @JvmField
        val MINERALS: TagKey<HTMaterialContents> = common(HTConstants.MINERALS)

        @JvmField
        val MINERALS_BERYL: TagKey<HTMaterialContents> = common(HTConstants.MINERALS, "beryl")

        @JvmField
        val MINERALS_ALUMINA: TagKey<HTMaterialContents> = common(HTConstants.MINERALS, "alumina")

        @JvmField
        val MINERALS_CORUNDUM: TagKey<HTMaterialContents> = common(HTConstants.MINERALS, "corundum")

        @JvmStatic
        private fun common(vararg path: String): TagKey<HTMaterialContents> = HTRegistries.Keys.MATERIAL_CONTENTS.createTagKey(HTConstants.COMMON.toId(*path))
    }
}
