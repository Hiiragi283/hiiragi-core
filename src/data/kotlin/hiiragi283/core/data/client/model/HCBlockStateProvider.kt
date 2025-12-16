package hiiragi283.core.data.client.model

import hiiragi283.core.HiiragiCore
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTBlockStateProvider
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCBlocks
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block

class HCBlockStateProvider(context: HTDataGenContext) : HTBlockStateProvider(context, HiiragiCoreAPI.MOD_ID) {
    override fun registerStatesAndModels() {
        // Material
        val storageBlock: HTMaterialPrefix = HCMaterialPrefixes.STORAGE_BLOCK
        for (material: HCMaterial in HCMaterial.entries) {
            val block: HTHolderLike<Block, Block> = HCBlocks.MATERIALS[storageBlock, material] ?: continue
            val textureId: ResourceLocation = HiiragiCoreAPI.id("block", storageBlock.name, material.asMaterialName())
            if (existTexture(textureId)) {
                altTextureBlock(block, textureId)
            } else {
                HiiragiCore.LOGGER.debug("Missing texture {} for {}", textureId, block.getId())
            }
        }
    }
}
