package hiiragi283.core.data.client.model

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTBlockStateProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import net.minecraft.resources.ResourceLocation

class HCBlockStateProvider(context: HTDataGenContext) : HTBlockStateProvider(HiiragiCoreAPI.MOD_ID, context) {
    override fun registerStatesAndModels() {
        registerMaterials()
        registerFluids()
    }

    private fun registerMaterials() {
        HCBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, material: HTMaterialKey, block: HTSimpleDeferredBlock) ->
            val textureId: ResourceLocation = HiiragiCoreAPI.id("block", prefix.name, material.name)
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
