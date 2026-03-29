package hiiragi283.core.api.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.util.emptyOptional
import hiiragi283.core.api.util.wrapOptional
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel

abstract class HTModelProvider(output: PackOutput, modId: String) : ModelProvider(output, modId) {
    abstract override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators)

    //    Extensions    //

    fun ItemModelGenerators.generateFlatItem(item: HTItemHolderLike<*>, template: ModelTemplate) {
        this.generateFlatItem(item.get(), template)
    }

    fun ItemModelGenerators.generateBucketItem(content: HTFluidContent, isDrip: Boolean) {
        val parentId: Identifier = when {
            isDrip -> "bucket_drip"
            else -> "bucket"
        }.let { HTConst.NEOFORGE.toId(HTConst.ITEM, it) }

        val baseMaterial = Material(HTConst.MINECRAFT.toId(HTConst.ITEM, "bucket"))

        this.itemModelOutput.accept(
            content.getBucket().get(),
            DynamicFluidContainerModel.Unbaked(
                DynamicFluidContainerModel.Textures(
                    baseMaterial.wrapOptional(),
                    baseMaterial.wrapOptional(),
                    Material(parentId).wrapOptional(),
                    emptyOptional(),
                ),
                content.get(),
                content.getFluidType().isLighterThanAir,
                true,
                true,
            ),
        )
    }
}
