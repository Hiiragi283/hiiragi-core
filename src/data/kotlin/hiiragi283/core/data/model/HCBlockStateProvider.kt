package hiiragi283.core.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.model.HTBlockStateProvider
import hiiragi283.core.api.data.model.trackTexture
import hiiragi283.core.api.data.model.withExistingParent
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.NetherWartBlock
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.data.ExistingFileHelper

class HCBlockStateProvider(fileHelper: ExistingFileHelper, output: PackOutput) : HTBlockStateProvider(fileHelper, output, HiiragiCoreAPI.MOD_ID) {
    override fun registerStatesAndModels() {
        trackBlock(HCBlocks.OIL_SAND)
        simpleBlockAndItem(HCBlocks.OIL_SAND)

        trackBlock(HCBlocks.OIL_SHALE)
        simpleBlockAndItem(HCBlocks.OIL_SHALE)

        registerCrops()

        // Misc
        for ((state: WeatheringCopper.WeatherState, block: SupplierWithId<Block>) in HCBlocks.COPPER_BASIN.weatheringMap) {
            val cutCopper: ResourceLocation = when (state) {
                WeatheringCopper.WeatherState.UNAFFECTED -> "cut_copper"
                WeatheringCopper.WeatherState.EXPOSED -> "exposed_cut_copper"
                WeatheringCopper.WeatherState.WEATHERED -> "weathered_cut_copper"
                WeatheringCopper.WeatherState.OXIDIZED -> "oxidized_cut_copper"
            }.let { HTConst.MINECRAFT.toId(HTConst.BLOCK, it) }
            val chiseledCopper: ResourceLocation = when (state) {
                WeatheringCopper.WeatherState.UNAFFECTED -> "chiseled_copper"
                WeatheringCopper.WeatherState.EXPOSED -> "exposed_chiseled_copper"
                WeatheringCopper.WeatherState.WEATHERED -> "weathered_chiseled_copper"
                WeatheringCopper.WeatherState.OXIDIZED -> "oxidized_chiseled_copper"
            }.let { HTConst.MINECRAFT.toId(HTConst.BLOCK, it) }
            registerCauldron(
                block,
                chiseledCopper,
                cutCopper,
                cutCopper,
                cutCopper,
            )
            val waxed: SupplierWithId<Block> = HCBlocks.COPPER_BASIN.waxedMap[state]!!
            simpleBlockAndItem(waxed, models().getExistingFile(block.blockId))
        }
        // Fluids
        HCFluids.REGISTER.asSequence().filterIsInstance<HTFluidContent.Flowing>().forEach(::liquidBlock)
    }

    private fun registerCauldron(
        block: SupplierWithId<Block>,
        top: ResourceLocation,
        side: ResourceLocation,
        bottom: ResourceLocation,
        inside: ResourceLocation,
    ) {
        simpleBlockAndItem(
            block,
            models()
                .withExistingParent(block, HiiragiCoreAPI.id(HTConst.BLOCK, "cauldron_template"))
                .texture("top", top)
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("inside", inside),
        )
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
