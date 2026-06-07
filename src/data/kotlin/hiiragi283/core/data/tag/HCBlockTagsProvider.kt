package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.tag.HTIdLikeTagsProvider
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.HTPartTagManager
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.registry.toLike
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTTagPrefix
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagAppender
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class HCBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTIdLikeTagsProvider<Block>(output, Registries.BLOCK, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        // Material
        tags(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.GLOWSTONE).addBlock(Blocks.GLOWSTONE)
        tags(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ).addBlock(Blocks.QUARTZ_BLOCK)
        tags(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST).addBlock(Blocks.AMETHYST_BLOCK)

        HCBlocks.RESOURCES.forEach { (part: HTMaterialPartKey, material: HTMaterialKey, block: HTIdLike) ->
            val prefix: HTTagPrefix = HTPartTagManager[part] ?: return@forEach
            tags(prefix, material).add(block)
        }
        // Mineable
        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(HCBlocks.WARPED_WART)
            .add(HCBlocks.CHOPPING_BOARD)
        tag(BlockTags.SWORD_EFFICIENT)
            .add(HCBlocks.WARPED_WART)

        val pickaxe: TagAppender<HTIdLike, Block> = tag(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence<HTIdLike> {
            yieldAll(HCBlocks.RESOURCES.values)

            yield(HCBlocks.FORGING_ANVIL)
            yieldAll(HCBlocks.COPPER_BASIN.allBlocks)
        }.forEach(pickaxe::add)
    }

    private fun IdAppender.addBlock(block: Block): IdAppender = this.add(block.toLike())
}
