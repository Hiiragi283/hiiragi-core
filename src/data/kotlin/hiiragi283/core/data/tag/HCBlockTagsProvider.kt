package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.tag.HTTagBuilder
import hiiragi283.lib.data.tag.HTTagsProvider
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.HTPartTagManager
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.resource.SimpleSupplierWithKey
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTTagPrefix
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.references.BlockItemIds
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block

class HCBlockTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTTagsProvider<Block>(output, Registries.BLOCK, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        // Material
        tags(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.GLOWSTONE).add(BlockItemIds.GLOWSTONE)
        tags(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ).add(BlockItemIds.QUARTZ_BLOCK)
        tags(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST).add(BlockItemIds.AMETHYST_BLOCK)

        HCBlocks.RESOURCES.forEach { (part: HTMaterialPartKey, material: HTMaterialKey, block: SimpleSupplierWithKey<Block>) ->
            val prefix: HTTagPrefix = HTPartTagManager[part] ?: return@forEach
            tags(prefix, material).add(block)
        }
        // Mineable
        builder(BlockTags.MINEABLE_WITH_AXE)
            .add(HCBlocks.WARPED_WART)
            .add(HCBlocks.CHOPPING_BOARD)
        builder(BlockTags.SWORD_EFFICIENT)
            .add(HCBlocks.WARPED_WART)

        val pickaxe: HTTagBuilder<Block> = builder(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence {
            yieldAll(HCBlocks.RESOURCES.values)

            yield(HCBlocks.FORGING_ANVIL)
            yieldAll(HCBlocks.COPPER_BASIN.asList())
        }.forEach(pickaxe::add)
        // Category
        HCBlocks.CONCRETE_SLABS.forEach {
            pickaxe.add(it)
            builder(BlockTags.SLABS).add(it)
        }
        HCBlocks.CONCRETE_STAIRS.forEach {
            pickaxe.add(it)
            builder(BlockTags.STAIRS).add(it)
        }
    }
}
