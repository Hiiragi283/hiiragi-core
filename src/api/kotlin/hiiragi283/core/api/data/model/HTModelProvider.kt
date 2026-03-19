package hiiragi283.core.api.data.model

import hiiragi283.core.api.registry.HTItemHolderLike
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.data.PackOutput

abstract class HTModelProvider(output: PackOutput, modId: String) : ModelProvider(output, modId) {
    abstract override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators)

    //    Extensions    //

    fun ItemModelGenerators.generateFlatItem(item: HTItemHolderLike<*>, template: ModelTemplate) {
        this.generateFlatItem(item.get(), template)
    }
}
