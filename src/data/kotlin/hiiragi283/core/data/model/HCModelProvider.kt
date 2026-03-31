package hiiragi283.core.data.model

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.model.HTModelProvider
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class HCModelProvider(output: PackOutput) : HTModelProvider(output, HiiragiCoreAPI.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        // Block
        blockModels.createCropBlock(HCBlocks.WARPED_WART.get(), BlockStateProperties.AGE_3, 0, 1, 1, 2)
        blockModels.createNonTemplateModelBlock(HCBlocks.CRUCIBLE.get()) // TODO
        // Fluid
        HCFluids.REGISTER
            .asBlockSequence()
            .map(HTBlockHolderLike<*>::get)
            .forEach(blockModels::createNonTemplateModelBlock)

        val dripFluids: List<HTFluidContent> = buildList {
            // Vanilla
            addAll(HCFluids.DYE.values)

            add(HCFluids.HONEY)
        }
        for (content: HTFluidContent in HCFluids.REGISTER.entries) {
            itemModels.generateBucketItem(content, content in dripFluids)
        }
        // Item
        itemModels.generateFlatItem(HCItems.IRIDESCENT_POWDER)
        itemModels.generateFlatItem(HCItems.ALMIGHTY_PICKAXE, ModelTemplates.FLAT_HANDHELD_ITEM)
    }
}
