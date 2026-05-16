package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagAppender
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.BlockTagsProvider

class HCBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : BlockTagsProvider(output, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun addTags(registries: HolderLookup.Provider) {
        tag(BlockTags.MINEABLE_WITH_AXE).add(HCBlocks.WARPED_WART.get())
        tag(BlockTags.SWORD_EFFICIENT).add(HCBlocks.WARPED_WART.get())

        val pickaxe: TagAppender<Block, Block> = tag(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence {
            yieldAll(HCBlocks.COPPER_BASIN.weatheringBlocks)
            yieldAll(HCBlocks.COPPER_BASIN.waxedBlocks)
        }.map { it.get() }.forEach(pickaxe::add)
    }
}
