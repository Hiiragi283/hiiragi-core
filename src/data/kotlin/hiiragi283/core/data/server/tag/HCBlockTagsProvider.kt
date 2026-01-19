package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class HCBlockTagsProvider(context: HTDataGenContext) : HTTagsProvider<Block>(HiiragiCoreAPI.MOD_ID, Registries.BLOCK, context) {
    companion object {
        @JvmField
        val VANILLA_STORAGE_BLOCKS: Map<HTMaterialKey, HTIdLike> = mapOf(
            VanillaMaterialKeys.GLOWSTONE to HTBlockHolderLike.Simple(Blocks.GLOWSTONE),
            VanillaMaterialKeys.AMETHYST to HTBlockHolderLike.Simple(Blocks.AMETHYST_BLOCK),
            VanillaMaterialKeys.QUARTZ to HTBlockHolderLike.Simple(Blocks.QUARTZ_BLOCK),
        )
    }

    override fun addTagsInternal(factory: BuilderFactory<Block>) {
        material(factory)
        tool(factory)
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Block>) {
        HTMaterialContentsAccess.INSTANCE.getBlockTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: HTIdLike) ->
            if (key.getNamespace() != modId) return@forEach
            addMaterial(factory, prefix, key).add(block)

            if (key == CommonMaterialKeys.COAL_COKE) {
                factory.apply(BlockTags.INFINIBURN_OVERWORLD).add(block)
            }
        }

        for ((key: HTMaterialKey, block: HTIdLike) in VANILLA_STORAGE_BLOCKS) {
            addMaterial(factory, CommonTagPrefixes.BLOCK, key).add(block)
        }
    }

    //    Tool    //

    private fun tool(factory: BuilderFactory<Block>) {
        factory
            .apply(BlockTags.MINEABLE_WITH_AXE)
            .add(HCBlocks.WARPED_WART)

        val pickaxe: HTTagBuilder<Block> = factory.apply(BlockTags.MINEABLE_WITH_PICKAXE)
        sequence {
            yieldAll(HTMaterialContentsAccess.INSTANCE.getAllBlocks().filter { it.getNamespace() == modId })
        }.forEach(pickaxe::add)

        factory
            .apply(BlockTags.SWORD_EFFICIENT)
            .add(HCBlocks.WARPED_WART)
    }
}
