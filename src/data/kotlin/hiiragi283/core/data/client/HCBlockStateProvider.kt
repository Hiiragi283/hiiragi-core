package hiiragi283.core.data.client

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTBlockStateProvider
import hiiragi283.core.api.data.model.trackTexture
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.LayeredCauldronBlock
import net.minecraft.world.level.block.NetherWartBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.ConfiguredModel

class HCBlockStateProvider(context: HTDataGenContext) : HTBlockStateProvider(HiiragiCoreAPI.MOD_ID, context) {
    override fun registerStatesAndModels() {
        trackBlock(HCBlocks.OIL_SAND)
        simpleBlockAndItem(HCBlocks.OIL_SAND)

        trackBlock(HCBlocks.OIL_SHALE)
        simpleBlockAndItem(HCBlocks.OIL_SHALE)

        registerCrops()

        // Device
        registerVariants(HCBlocks.LATEX_CAULDRON) { _, state: BlockState ->
            val suffix: String = when (val level: Int = state.getValue(LayeredCauldronBlock.LEVEL)) {
                3 -> "_full"
                else -> "_level$level"
            }
            ConfiguredModel
                .builder()
                .modelFile(models().getExistingFile(HTConst.MINECRAFT.toId("block/water_cauldron").withSuffix(suffix)))
                .build()
        }

        // Fluids
        HCFluids.REGISTER.asSequence().forEach(::liquidBlock)
    }

    private fun registerCrops() {
        registerVariants(HCBlocks.WARPED_WART) { block, state: BlockState ->
            val age: Int = when (state.getValue(NetherWartBlock.AGE)) {
                0 -> 0
                1 -> 1
                2 -> 1
                else -> 2
            }
            val id: ResourceLocation = block.blockId.withSuffix("_stage$age")
            models().trackTexture(id)
            ConfiguredModel
                .builder()
                .modelFile(
                    models()
                        .withExistingParent(id.path, "crop")
                        .texture("crop", id)
                        .renderType("cutout"),
                ).build()
        }

        trackItem(HCBlocks.WARPED_WART)
        itemModels().basicItem(HCBlocks.WARPED_WART.getId())
    }
}
