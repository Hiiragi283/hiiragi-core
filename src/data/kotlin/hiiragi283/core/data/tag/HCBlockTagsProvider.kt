package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.lib.data.tag.HTIdLikeTagsProvider
import hiiragi283.lib.resource.HTIdLike
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagAppender
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block

class HCBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTIdLikeTagsProvider<Block>(output, Registries.BLOCK, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun addTags(registries: HolderLookup.Provider) {
        tag(BlockTags.MINEABLE_WITH_AXE).add(HCBlocks.WARPED_WART)
        tag(BlockTags.SWORD_EFFICIENT).add(HCBlocks.WARPED_WART)

        val pickaxe: TagAppender<HTIdLike, Block> = tag(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence {
            yieldAll(HCBlocks.COPPER_BASIN.weatheringBlocks)
            yieldAll(HCBlocks.COPPER_BASIN.waxedBlocks)
        }.forEach(pickaxe::add)
    }
}
