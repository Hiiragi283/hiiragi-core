package hiiragi283.core.data.client.model

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTBlockStateProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.NetherWartBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.ConfiguredModel

class HCBlockStateProvider(context: HTDataGenContext) : HTBlockStateProvider(HiiragiCoreAPI.MOD_ID, context) {
    override fun registerStatesAndModels() {
        registerMaterials()
        registerCrops()

        // Fluids
        HCFluids.REGISTER.entries.forEach(::liquidBlock)
    }

    private fun registerMaterials() {
        HCBlocks.MATERIALS.forEach { (prefix: HTMaterialPrefix, material: HTMaterialKey, block: HTSimpleDeferredBlock) ->
            val textureId: ResourceLocation = HiiragiCoreAPI.id("block", prefix.name, material.name)
            existTexture(block, textureId, ::altTextureBlock)
        }
    }

    private fun registerCrops() {
        getVariantBuilder(HCBlocks.WARPED_WART.get())
            .forAllStates { state: BlockState ->
                val age: Int = when (state.getValue(NetherWartBlock.AGE)) {
                    0 -> 0
                    1 -> 1
                    2 -> 1
                    else -> 2
                }
                val id: ResourceLocation = HCBlocks.WARPED_WART.id.withSuffix("_stage$age")
                ConfiguredModel
                    .builder()
                    .modelFile(
                        models()
                            .withExistingParent(id.path, "crop")
                            // .texture("crop", id.withPrefix("block/"))
                            .renderType("cutout"),
                    ).build()
            }
        itemModels().basicItem(HCBlocks.WARPED_WART.id)
    }
}
