package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.lib.data.tag.HTIdLikeTagsProvider
import hiiragi283.lib.registry.toLike
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.tag.HTCommonTags
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagAppender
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

class HCBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTIdLikeTagsProvider<Block>(output, Registries.BLOCK, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun addTags(registries: HolderLookup.Provider) {
        // Material
        tags(Tags.Blocks.STORAGE_BLOCKS, HTCommonTags.Blocks.STORAGE_BLOCKS_CHARCOAL).add(HCBlocks.CHARCOAL_BLOCK)
        tags(Tags.Blocks.STORAGE_BLOCKS, HTCommonTags.Blocks.STORAGE_BLOCKS_GLOWSTONE).addBlock(Blocks.GLOWSTONE)
        tags(Tags.Blocks.STORAGE_BLOCKS, HTCommonTags.Blocks.STORAGE_BLOCKS_QUARTZ).addBlock(Blocks.QUARTZ_BLOCK)
        tags(Tags.Blocks.STORAGE_BLOCKS, HTCommonTags.Blocks.STORAGE_BLOCKS_AMETHYST).addBlock(Blocks.AMETHYST_BLOCK)
        tags(Tags.Blocks.STORAGE_BLOCKS, HTCommonTags.Blocks.STORAGE_BLOCKS_ECHO).add(HCBlocks.ECHO_BLOCK)
        // Mineable
        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(HCBlocks.WARPED_WART)
            .add(HCBlocks.CHOPPING_BOARD)
        tag(BlockTags.SWORD_EFFICIENT)
            .add(HCBlocks.WARPED_WART)

        val pickaxe: TagAppender<HTIdLike, Block> = tag(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence<HTIdLike> {
            yield(HCBlocks.CHARCOAL_BLOCK)
            yield(HCBlocks.ECHO_BLOCK)

            yieldAll(HCBlocks.COPPER_BASIN.allBlocks)
        }.forEach(pickaxe::add)
    }

    private fun IdAppender<Block>.addBlock(block: Block): IdAppender<Block> = this.add(block.toLike())
}
