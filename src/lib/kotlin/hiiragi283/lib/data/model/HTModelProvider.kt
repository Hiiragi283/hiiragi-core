package hiiragi283.lib.data.model

import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.MultiVariant
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block

abstract class HTModelProvider(output: PackOutput, modId: String) : ModelProvider(output, modId) {
    abstract override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators)

    //    Block    //

    fun BlockModelGenerators.createSimple(block: Block, modelId: Identifier) {
        this.createSimple(block, BlockModelGenerators.plainVariant(modelId))
    }

    fun BlockModelGenerators.createSimple(block: Block, variant: MultiVariant) {
        this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, variant))
    }

    //    Item    //
}
