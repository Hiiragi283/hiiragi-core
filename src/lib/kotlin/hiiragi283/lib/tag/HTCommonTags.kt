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

data object HTCommonTags {
    // Ores

    // Storage Blocks
    // Vanilla
    @JvmStatic
    val STORAGE_BLOCKS_CHARCOAL: RawTagKey = common(HTConstants.STORAGE_BLOCKS, "charcoal")

    @JvmStatic
    val STORAGE_BLOCKS_GLOWSTONE: RawTagKey = common(HTConstants.STORAGE_BLOCKS, "glowstone")

    @JvmStatic
    val STORAGE_BLOCKS_QUARTZ: RawTagKey = common(HTConstants.STORAGE_BLOCKS, "quartz")

    @JvmStatic
    val STORAGE_BLOCKS_AMETHYST: RawTagKey = common(HTConstants.STORAGE_BLOCKS, "amethyst")

    @JvmStatic
    val STORAGE_BLOCKS_ECHO: RawTagKey = common(HTConstants.STORAGE_BLOCKS, "echo")

    // Common
    @JvmStatic
    val STORAGE_BLOCKS_IRIDIUM: RawTagKey = common(HTConstants.STORAGE_BLOCKS, "iridium")

    @JvmStatic
    private fun common(vararg path: String): RawTagKey = RawTagKey.common(*path)

    data object Blocks {
        @JvmStatic
        private fun common(vararg path: String): TagKey<Block> = BlockTags.create(HTConstants.COMMON.toId(*path))
    }

    data object Items {
        // Dusts
        // Vanilla
        @JvmField
        val DUSTS_COPPER: TagKey<Item> = common(HTConstants.DUSTS, "copper")

        @JvmField
        val DUSTS_IRON: TagKey<Item> = common(HTConstants.DUSTS, "iron")

        @JvmField
        val DUSTS_GOLD: TagKey<Item> = common(HTConstants.DUSTS, "gold")

        @JvmField
        val DUSTS_NETHERITE: TagKey<Item> = common(HTConstants.DUSTS, "netherite")

        @JvmField
        val DUSTS_OBSIDIAN: TagKey<Item> = common(HTConstants.DUSTS, "obsidian")

        // Common
        @JvmField
        val DUSTS_IRIDIUM: TagKey<Item> = common(HTConstants.DUSTS, "iridium")

        // Fuels

        // Gears

        // Gems
        // Vanilla
        @JvmField
        val GEMS_ECHO: TagKey<Item> = common(HTConstants.GEMS, "echo")

        // Ingots
        // Common
        @JvmField
        val INGOTS_IRIDIUM: TagKey<Item> = common(HTConstants.INGOTS, "iridium")

        // Nuggets
        // Vanilla
        @JvmField
        val NUGGETS_NETHERITE: TagKey<Item> = common(HTConstants.NUGGETS, "netherite")

        // Common
        @JvmField
        val NUGGETS_IRIDIUM: TagKey<Item> = common(HTConstants.NUGGETS, "iridium")

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
