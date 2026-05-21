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
    data object Blocks {
        // Ores

        // Storage Blocks
        @JvmField
        val STORAGE_BLOCKS_CHARCOAL: TagKey<Block> = common(HTConstants.STORAGE_BLOCKS, "charcoal")

        @JvmField
        val STORAGE_BLOCKS_GLOWSTONE: TagKey<Block> = common(HTConstants.STORAGE_BLOCKS, "glowstone")

        @JvmField
        val STORAGE_BLOCKS_QUARTZ: TagKey<Block> = common(HTConstants.STORAGE_BLOCKS, "quartz")

        @JvmField
        val STORAGE_BLOCKS_AMETHYST: TagKey<Block> = common(HTConstants.STORAGE_BLOCKS, "amethyst")

        @JvmField
        val STORAGE_BLOCKS_ECHO: TagKey<Block> = common(HTConstants.STORAGE_BLOCKS, "echo")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Block> = BlockTags.create(HTConstants.COMMON.toId(*path))
    }

    data object Items {
        // Ores

        // Storage Blocks
        @JvmField
        val STORAGE_BLOCKS_CHARCOAL: TagKey<Item> = from(Blocks.STORAGE_BLOCKS_CHARCOAL)

        @JvmField
        val STORAGE_BLOCKS_GLOWSTONE: TagKey<Item> = from(Blocks.STORAGE_BLOCKS_GLOWSTONE)

        @JvmField
        val STORAGE_BLOCKS_QUARTZ: TagKey<Item> = from(Blocks.STORAGE_BLOCKS_QUARTZ)

        @JvmField
        val STORAGE_BLOCKS_AMETHYST: TagKey<Item> = from(Blocks.STORAGE_BLOCKS_AMETHYST)

        @JvmField
        val STORAGE_BLOCKS_ECHO: TagKey<Item> = from(Blocks.STORAGE_BLOCKS_ECHO)

        // Dusts
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

        // Fuels

        // Gears

        // Gems
        @JvmField
        val GEMS_ECHO: TagKey<Item> = common(HTConstants.GEMS, "echo")

        // Ingots

        // Nuggets
        @JvmField
        val NUGGETS_NETHERITE: TagKey<Item> = common(HTConstants.NUGGETS, "netherite")

        // Pearls

        @JvmStatic
        private fun from(other: TagKey<*>): TagKey<Item> = ItemTags.create(other.location())

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
