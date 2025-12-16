package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

class HCBlockTagsProvider(context: HTDataGenContext) : HTTagsProvider<Block>(HiiragiCoreAPI.MOD_ID, Registries.BLOCK, context) {
    companion object {
        @JvmField
        val VANILLA_STORAGE_BLOCKS: Map<HCMaterial, HTItemHolderLike<*>> = mapOf(
            HCMaterial.Dusts.GLOWSTONE to Blocks.GLOWSTONE.toHolderLike(),
            HCMaterial.Gems.AMETHYST to Blocks.AMETHYST_BLOCK.toHolderLike(),
            HCMaterial.Gems.QUARTZ to Blocks.QUARTZ_BLOCK.toHolderLike(),
        )
    }

    override fun addTagsInternal(factory: BuilderFactory<Block>) {
        material(factory)
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Block>) {
        HCBlocks.MATERIAL.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, block: HTIdLike) ->
            addMaterial(factory, prefix, key).add(block)
        }

        for ((material: HCMaterial, block: HTIdLike) in VANILLA_STORAGE_BLOCKS) {
            addMaterial(factory, HCMaterialPrefixes.STORAGE_BLOCK, material).add(block)
        }
    }
}
