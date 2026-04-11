package hiiragi283.core.data.client

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.block.HTWeatheringBlocks
import hiiragi283.core.api.block.HTWeatheringLevel
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTBlockStateProvider
import hiiragi283.core.api.data.model.trackTexture
import hiiragi283.core.api.data.model.withExistingParent
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import net.minecraft.resources.ResourceLocation
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

        // Misc
        for (level: HTWeatheringLevel in HTWeatheringLevel.entries) {
            val (base: HTBlockHolderLike<*>, waxed: HTBlockHolderLike<*>) = HCBlocks.COPPER_BASINS[level] ?: continue
            val cutCopper: ResourceLocation = HTWeatheringBlocks.CUT_COPPER[level]?.first?.blockId ?: continue
            val chiseledCopper: ResourceLocation = HTWeatheringBlocks.CHISELED_COPPER[level]?.first?.blockId ?: continue
            registerCauldron(
                base,
                chiseledCopper,
                cutCopper,
                cutCopper,
                cutCopper,
            )

            simpleBlockAndItem(waxed, models().getExistingFile(base.blockId))
        }
        with(HCBlocks.FORGING_ANVIL) {
            val blockId: ResourceLocation = this.blockId
            val topId: ResourceLocation = blockId.withSuffix("_top")
            models().trackTexture(blockId)
            models().trackTexture(topId)

            simpleBlockAndItem(
                this,
                models()
                    .withExistingParent(this, HTConst.MINECRAFT.toId(HTConst.BLOCK, "anvil"))
                    .texture("top", topId)
                    .texture("body", blockId)
                    .texture("particle", blockId),
            )
        }
        // Fluids
        HCFluids.REGISTER.asSequence().forEach(::liquidBlock)
    }

    private fun registerCauldron(
        block: HTBlockHolderLike<*>,
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
