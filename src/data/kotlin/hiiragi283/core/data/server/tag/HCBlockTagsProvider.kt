package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class HCBlockTagsProvider(context: HTDataGenContext) : HTTagsProvider.DataGen<Block>(HiiragiCoreAPI.MOD_ID, Registries.BLOCK, context) {
    companion object {
        @JvmField
        val VANILLA_STORAGE_BLOCKS: Map<HTMaterialKey, Block> = mapOf(
            VanillaMaterialKeys.GLOWSTONE to Blocks.GLOWSTONE,
            VanillaMaterialKeys.AMETHYST to Blocks.AMETHYST_BLOCK,
            VanillaMaterialKeys.QUARTZ to Blocks.QUARTZ_BLOCK,
        )
    }

    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Block>) {
        material(factory)
        tool(factory)
    }

    //    Material    //

    private fun material(factory: HTTagsProvider.BuilderFactory<Block>) {
        for ((key: HTMaterialKey, block: Block) in VANILLA_STORAGE_BLOCKS) {
            addMaterial(factory, CommonTagPrefixes.BLOCK, key).add(HTBlockHolderLike.of(block))
        }
    }

    //    Tool    //

    private fun tool(factory: HTTagsProvider.BuilderFactory<Block>) {
        factory
            .apply(BlockTags.MINEABLE_WITH_AXE)
            .add(HCBlocks.WARPED_WART)

        val pickaxe: HTTagBuilder<Block> = factory.apply(BlockTags.MINEABLE_WITH_PICKAXE)

        factory
            .apply(BlockTags.SWORD_EFFICIENT)
            .add(HCBlocks.WARPED_WART)
    }
}
