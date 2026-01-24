package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTBlockTagsProvider
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class HCBlockTagsProvider(context: HTDataGenContext) : HTBlockTagsProvider(HiiragiCoreAPI.MOD_ID, context) {
    companion object {
        @JvmField
        val VANILLA_STORAGE_BLOCKS: Map<HTMaterialKey, Block> = mapOf(
            VanillaMaterialKeys.GLOWSTONE to Blocks.GLOWSTONE,
            VanillaMaterialKeys.AMETHYST to Blocks.AMETHYST_BLOCK,
            VanillaMaterialKeys.QUARTZ to Blocks.QUARTZ_BLOCK,
        )
    }

    override fun addTagsInternal(factory: BuilderFactory<Block>) {
        material(factory)
        tool(factory)
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Block>) {
        addMaterials(factory) { (_, key: HTMaterialKey, block: HTIdLike) ->
            if (key == CommonMaterialKeys.COAL_COKE) {
                factory.apply(BlockTags.INFINIBURN_OVERWORLD).add(block)
            }
        }

        for ((key: HTMaterialKey, block: Block) in VANILLA_STORAGE_BLOCKS) {
            addMaterial(factory, CommonTagPrefixes.BLOCK, key).addBlock(block)
        }
    }

    //    Tool    //

    private fun tool(factory: BuilderFactory<Block>) {
        factory
            .apply(BlockTags.MINEABLE_WITH_AXE)
            .add(HCBlocks.WARPED_WART)

        val pickaxe: HTTagBuilder<Block> = factory.apply(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence {
            yieldAll(contents.getAllBlocks().filter { it.namespace == modId })
        }.forEach(pickaxe::add)

        factory
            .apply(BlockTags.SWORD_EFFICIENT)
            .add(HCBlocks.WARPED_WART)
    }
}
