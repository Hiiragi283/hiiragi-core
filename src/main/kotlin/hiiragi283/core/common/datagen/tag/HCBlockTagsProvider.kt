package hiiragi283.core.common.datagen.tag

import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

object HCBlockTagsProvider : HTTagsProvider<Block>(Registries.BLOCK) {
    @JvmField
    val VANILLA_STORAGE_BLOCKS: Map<HTMaterialKey, Block> = mapOf(
        VanillaMaterialKeys.GLOWSTONE to Blocks.GLOWSTONE,
        VanillaMaterialKeys.AMETHYST to Blocks.AMETHYST_BLOCK,
        VanillaMaterialKeys.QUARTZ to Blocks.QUARTZ_BLOCK,
    )

    override fun addTagsInternal(factory: BuilderFactory<Block>) {
        // Materials
        contents.getBlockTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: HTIdLike) ->
            addMaterial(factory, prefix, key).add(block)
        }

        for ((key: HTMaterialKey, block: Block) in VANILLA_STORAGE_BLOCKS) {
            addMaterial(factory, CommonTagPrefixes.BLOCK, key).add(HTBlockHolderLike.of(block))
        }
        // Tool
        factory
            .apply(BlockTags.MINEABLE_WITH_AXE)
            .add(HCBlocks.WARPED_WART)

        val pickaxe: HTTagBuilder<Block> = factory.apply(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence {
            yieldAll(contents.getAllBlocks())
        }.forEach(pickaxe::add)

        factory
            .apply(BlockTags.SWORD_EFFICIENT)
            .add(HCBlocks.WARPED_WART)
    }
}
