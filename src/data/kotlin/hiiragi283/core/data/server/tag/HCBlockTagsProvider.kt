package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block

class HCBlockTagsProvider(context: HTDataGenContext) : HTTagsProvider.DataGen<Block>(HiiragiCoreAPI.MOD_ID, Registries.BLOCK, context) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Block>) {
        // Mineable
        factory
            .apply(BlockTags.MINEABLE_WITH_AXE)
            .add(HCBlocks.WARPED_WART)

        val pickaxe: HTTagBuilder<Block> = factory
            .apply(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(HCBlocks.OIL_SHALE)
            .add(HCBlocks.TREE_TAP)
            .add(HCBlocks.FORGING_ANVIL)
        HCBlocks.COPPER_BASINS.forEach { base: HTIdLike, waxed: HTIdLike ->
            pickaxe.add(base)
            pickaxe.add(waxed)
        }

        factory
            .apply(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(HCBlocks.OIL_SAND)

        factory
            .apply(BlockTags.SWORD_EFFICIENT)
            .add(HCBlocks.WARPED_WART)
        // Other
        factory
            .apply(HiiragiCoreTags.Blocks.LATEX_DRIPPING_LOGS)
            .addTag(BlockTags.ACACIA_LOGS)
            .addTag(BlockTags.JUNGLE_LOGS)
            .addTag(BlockTags.MANGROVE_LOGS)
    }
}
