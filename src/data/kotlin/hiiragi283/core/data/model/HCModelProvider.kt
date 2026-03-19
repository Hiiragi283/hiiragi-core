package hiiragi283.core.data.model

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.model.HTModelProvider
import hiiragi283.core.setup.HCBlocks
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.state.properties.BlockStateProperties

class HCModelProvider(output: PackOutput) : HTModelProvider(output, HiiragiCoreAPI.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        // Block
        blockModels.createCropBlock(HCBlocks.WARPED_WART.get(), BlockStateProperties.AGE_3, 0, 1, 1, 2)
    }
}
