package hiiragi283.core.data.client.model

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTBlockStateProvider
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block

class HCBlockStateProvider(context: HTDataGenContext) : HTBlockStateProvider(context, HiiragiCoreAPI.MOD_ID) {
    override fun registerStatesAndModels() {
        registerMaterials()
        registerFluids()
    }

    private fun registerMaterials() {
        val storageBlock: HTMaterialPrefix = HCMaterialPrefixes.STORAGE_BLOCK
        for (material: HCMaterial in HCMaterial.entries) {
            val block: HTHolderLike<Block, Block> = HCBlocks.MATERIALS[storageBlock, material] ?: continue
            val textureId: ResourceLocation = HiiragiCoreAPI.id("block", storageBlock.name, material.asMaterialName())
            existTexture(block, textureId, ::altTextureBlock)
        }
    }

    private fun registerFluids() {
        for (content: HTFluidContent<*, *, *> in HCFluids.REGISTER.entries) {
            simpleBlock(
                content.block.get(),
                models()
                    .getBuilder(content.blockId)
                    .texture("particle", vanillaId("block", "water_still")),
            )
        }
    }
}
