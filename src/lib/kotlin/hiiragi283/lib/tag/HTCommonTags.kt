package hiiragi283.lib.tag

import hiiragi283.lib.HTConstants
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

        @JvmField
        val STORAGE_BLOCKS_RESIN: TagKey<Block> = common(HTConstants.STORAGE_BLOCKS, "resin")

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

        @JvmField
        val STORAGE_BLOCKS_RESIN: TagKey<Item> = from(Blocks.STORAGE_BLOCKS_RESIN)

        // Dusts

        // Fuels

        // Gears

        // Gems
        @JvmField
        val GEMS_ECHO: TagKey<Item> = common(HTConstants.GEMS, "echo")

        // Ingots

        // Nuggets

        // Pearls

        @JvmStatic
        private fun from(other: TagKey<*>): TagKey<Item> = ItemTags.create(other.location())

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = ItemTags.create(HTConstants.COMMON.toId(*path))
    }
}
