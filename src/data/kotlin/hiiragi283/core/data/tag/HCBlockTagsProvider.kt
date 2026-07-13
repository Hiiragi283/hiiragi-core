package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class HCBlockTagsProvider(fileHelper: ExistingFileHelper, output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTTagsProvider.DataGen<Block>(fileHelper, output, Registries.BLOCK, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Block>) {
        // Mineable
        factory
            .apply(BlockTags.MINEABLE_WITH_AXE)
            .add(HCBlocks.WARPED_WART)

        val pickaxe: HTTagBuilder<Block> = factory
            .apply(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(HCBlocks.OIL_SHALE)
            .add(HCBlocks.TREE_TAP)
        HCBlocks.COPPER_BASIN.allCoppers.forEach(pickaxe::add)

        factory
            .apply(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(HCBlocks.OIL_SAND)

        factory
            .apply(BlockTags.SWORD_EFFICIENT)
            .add(HCBlocks.WARPED_WART)
        // Other
        factory
            .apply(HiiragiCoreTags.Blocks.INCORRECT_FOR_ALMIGHTY_PICKAXE)

        factory
            .apply(HiiragiCoreTags.Blocks.LATEX_DRIPPING_LOGS)
            .addTag(BlockTags.ACACIA_LOGS)
            .addTag(BlockTags.JUNGLE_LOGS)
            .addTag(BlockTags.MANGROVE_LOGS)
    }
}
