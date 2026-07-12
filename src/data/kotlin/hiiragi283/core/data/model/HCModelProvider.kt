package hiiragi283.core.data.model

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.lib.HTConstants
import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.color.get
import hiiragi283.lib.copper.HTCopperPhase
import hiiragi283.lib.copper.get
import hiiragi283.lib.data.model.HTModelProvider
import hiiragi283.lib.data.model.createBlock
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.resource.blockId
import hiiragi283.lib.resource.vanillaId
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.data.PackOutput
import net.minecraft.references.BlockItemIds
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeatheringCopperCollection
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class HCModelProvider(output: PackOutput) : HTModelProvider(output, HiiragiCoreAPI.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        // Fluids
        val dripFluids: Set<HTFluidContent> = buildSet {
            // Vanilla
            HCFluids.DYES.forEach(::add)

            add(HCFluids.HONEY)
        }
        for (content: HTFluidContent in HCFluids.REGISTER.asSequence()) {
            // Item
            itemModels.generateBucketItem(content, content in dripFluids)
            // Block
            if (content is HTFluidContent.Flowing) {
                content.blockHolder?.let { blockModels.createFluid(it) }
            }
        }

        // Block
        registerBlockModels(blockModels)
        // Item
        registerItemModels(itemModels)
    }

    private fun registerBlockModels(generators: BlockModelGenerators) {
        // Resources
        HCBlocks.RESOURCES.values.forEach { generators.createTrivialCube(it.get()) }

        // Concrete Stairs
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            val concrete = HTKeyLike { BlockItemIds.CONCRETE[color].block() }
            val texture = Material(concrete.blockId)
            generators.createSlab(HCBlocks.CONCRETE_SLABS[color], concrete.blockId, texture)
            generators.createStairs(HCBlocks.CONCRETE_STAIRS[color], texture)
        }
        // Warped Wart
        generators.createCropBlock(HCBlocks.WARPED_WART.get(), BlockStateProperties.AGE_3, 0, 1, 1, 2)
        // Chopping Board
        generators.createAltModel(HCBlocks.CHOPPING_BOARD)
        generators.createAltModel(HCBlocks.FORGING_ANVIL)
        // Copper Basins
        generators.createCopperBasin(HCBlocks.COPPER_BASIN.weathering)
        generators.createCopperBasin(HCBlocks.COPPER_BASIN.waxed)
    }

    private fun registerItemModels(generators: ItemModelGenerators) {
        // Resources
        HCItems.RESOURCES.values.forEach { generators.generateFlatItem(it) }

        generators.generateFlatItem(HCItems.ELDER_HEART)
        // Ingredients
        generators.generateFlatItem(HCItems.SYNTHETIC_FEATHER)
        generators.generateFlatItem(HCItems.SYNTHETIC_FIBER)
        generators.generateFlatItem(HCItems.SYNTHETIC_LEATHER)

        // End Game
        generators.generateFlatItem(HCItems.IRIDESCENT_POWDER)
        generators.generateFlatItem(HCItems.AMBROSIA)
        generators.generateFlatItem(HCItems.ETERNAL_UPGRADE)
        generators.generateLayeredItem(HCItems.POTION_OF_INFINITY, vanillaId(HTConstants.ITEM, "potion"), vanillaId(HTConstants.ITEM, "potion_overlay"))
        generators.generateFlatItem(HCItems.RING_OF_HYPERION)
    }

    private fun BlockModelGenerators.createCopperBasin(blocks: WeatheringCopperCollection.ByState<out SupplierWithId<Block>>) {
        for (phase: HTCopperPhase in HTCopperPhase.entries) {
            val block: SupplierWithId<Block> = blocks[phase]
            val cutCopper: Material = vanillaId(HTConstants.BLOCK, phase.createPath("cut_copper")).let(::Material)
            val chiseledCopper: Material = vanillaId(HTConstants.BLOCK, phase.createPath("chiseled_copper")).let(::Material)
            val modelId: Identifier = HCModelTemplates.CAULDRON.createBlock(
                block,
                TextureMapping()
                    .put(TextureSlot.TOP, chiseledCopper)
                    .put(TextureSlot.SIDE, cutCopper)
                    .put(TextureSlot.BOTTOM, cutCopper)
                    .put(TextureSlot.INSIDE, cutCopper),
                this.modelOutput,
            )
            this.createSimple(block.get(), modelId)
        }
    }
}
