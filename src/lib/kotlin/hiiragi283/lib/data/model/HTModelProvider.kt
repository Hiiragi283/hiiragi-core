package hiiragi283.lib.data.model

import hiiragi283.lib.HTConstants
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.resource.blockId
import hiiragi283.lib.resource.itemId
import hiiragi283.lib.resource.toId
import java.util.Optional
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.MultiVariant
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel

abstract class HTModelProvider(output: PackOutput, modId: String) : ModelProvider(output, modId) {
    abstract override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators)

    //    Block    //

    fun BlockModelGenerators.registerSimple(block: Block, modelId: Identifier) {
        this.registerSimple(block, BlockModelGenerators.plainVariant(modelId))
    }

    fun BlockModelGenerators.registerSimple(block: Block, variant: MultiVariant) {
        this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, variant))
    }

    fun BlockModelGenerators.registerFluid(fluidBlock: SupplierWithId<Block>) {
        this.registerSimple(
            fluidBlock.get(),
            HTModelTemplates.FLUID_BLOCK.create(
                fluidBlock.blockId,
                TextureMapping.particle(Material(HTConstants.MINECRAFT.toId(HTConstants.BLOCK, "water_still"))),
                this.modelOutput,
            ),
        )
    }

    //    Item    //

    fun ItemModelGenerators.generateFlatItem(item: SupplierWithId<Item>, layer: Identifier = item.itemId, template: ModelTemplate = ModelTemplates.FLAT_ITEM) {
        this.itemModelOutput.accept(item.get(), ItemModelUtils.plainModel(this.createFlatItemModel(item, layer, template)))
    }

    fun ItemModelGenerators.createFlatItemModel(
        item: HTIdLike,
        layer: Identifier = item.itemId,
        template: ModelTemplate = ModelTemplates.FLAT_ITEM,
    ): Identifier = template.create(item.itemId, TextureMapping.layer0(Material(layer)), this.modelOutput)

    fun ItemModelGenerators.generateBucketItem(content: HTFluidContent, isDrip: Boolean) {
        fun material(namespace: String, path: String): Optional<Material> = Optional.of(namespace.toId(HTConstants.ITEM, path).let(::Material))

        val suffix: String = when (isDrip) {
            true -> "_drip"
            false -> ""
        }

        this.itemModelOutput.accept(
            content.bucketHolder.get(),
            DynamicFluidContainerModel.Unbaked(
                DynamicFluidContainerModel.Textures(
                    material(HTConstants.MINECRAFT, "bucket"),
                    material(HTConstants.MINECRAFT, "bucket"),
                    material(HTConstants.NEOFORGE, "mask/bucket_fluid$suffix"),
                    Optional.empty(), // material(HTConstants.NEOFORGE, "mask/bucket_fluid_cover$suffix"),
                ),
                content.get(),
                content.getFluidType().isLighterThanAir,
                true,
                false,
            ),
        )
    }
}
