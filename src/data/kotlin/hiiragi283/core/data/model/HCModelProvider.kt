package hiiragi283.core.data.model

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.model.HTModelProvider
import hiiragi283.lib.registry.HTCopperMap
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.resource.blockId
import hiiragi283.lib.resource.toId
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class HCModelProvider(output: PackOutput) : HTModelProvider(output, HiiragiCoreAPI.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        // Block Resources
        blockModels.createTrivialCube(HCBlocks.CHARCOAL_BLOCK.get())
        blockModels.createTrivialCube(HCBlocks.ECHO_BLOCK.get())

        // Warped Wart
        blockModels.createCropBlock(HCBlocks.WARPED_WART.get(), BlockStateProperties.AGE_3, 0, 1, 1, 2)
        // Copper Basin
        blockModels.registerCopperBasin(HCBlocks.COPPER_BASIN.weathering)
        blockModels.registerCopperBasin(HCBlocks.COPPER_BASIN.waxed)

        // Fluids
        val dripFluids: List<HTFluidContent> = buildList {
            // Vanilla
            addAll(HCFluids.DyeContents.values)

            add(HCFluids.HONEY)
        }
        for (content: HTFluidContent in HCFluids.REGISTER.asSequence()) {
            // Item
            itemModels.generateBucketItem(content, content in dripFluids)
            // Block
            if (content is HTFluidContent.Flowing) {
                content.blockHolder?.let { blockModels.registerFluid(it) }
            }
        }

        // Item Resources
        itemModels.generateFlatItem(HCItems.NETHERITE_NUGGET)
    }

    private fun BlockModelGenerators.registerCopperBasin(map: HTCopperMap<SupplierWithId<Block>>) {
        for ((state: WeatheringCopper.WeatherState, block: SupplierWithId<Block>) in map) {
            val cutCopper: Material = when (state) {
                WeatheringCopper.WeatherState.UNAFFECTED -> "cut_copper"
                WeatheringCopper.WeatherState.EXPOSED -> "exposed_cut_copper"
                WeatheringCopper.WeatherState.WEATHERED -> "weathered_cut_copper"
                WeatheringCopper.WeatherState.OXIDIZED -> "oxidized_cut_copper"
            }.let { HTConstants.MINECRAFT.toId(HTConstants.BLOCK, it) }
                .let(::Material)
            val chiseledCopper: Material = when (state) {
                WeatheringCopper.WeatherState.UNAFFECTED -> "chiseled_copper"
                WeatheringCopper.WeatherState.EXPOSED -> "exposed_chiseled_copper"
                WeatheringCopper.WeatherState.WEATHERED -> "weathered_chiseled_copper"
                WeatheringCopper.WeatherState.OXIDIZED -> "oxidized_chiseled_copper"
            }.let { HTConstants.MINECRAFT.toId(HTConstants.BLOCK, it) }
                .let(::Material)
            val modelId: Identifier = HCModelTemplates.CAULDRON.create(
                block.blockId,
                TextureMapping()
                    .put(TextureSlot.TOP, chiseledCopper)
                    .put(TextureSlot.SIDE, cutCopper)
                    .put(TextureSlot.BOTTOM, cutCopper)
                    .put(TextureSlot.INSIDE, cutCopper),
                this.modelOutput,
            )
            this.registerSimple(block.get(), modelId)
        }
    }
}
