package hiiragi283.core.api.data.model

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.client.model.generators.ModelProvider.TEXTURE
import net.neoforged.neoforge.common.data.ExistingFileHelper

abstract class HTBlockStateProvider(context: HTDataGenContext, modid: String) :
    BlockStateProvider(context.output, modid, context.fileHelper) {
    protected val fileHelper: ExistingFileHelper = context.fileHelper

    //    Extensions    //

    protected fun <BUILDER : ModelBuilder<BUILDER>, PROVIDER : ModelProvider<BUILDER>> PROVIDER.getBuilder(id: ResourceLocation): BUILDER =
        this.getBuilder(id.toString())

    protected fun <BUILDER : ModelBuilder<BUILDER>, PROVIDER : ModelProvider<BUILDER>> PROVIDER.getBuilder(like: HTIdLike): BUILDER =
        this.getBuilder(like.getId())

    protected fun existTexture(id: ResourceLocation): Boolean = fileHelper.exists(id, TEXTURE)

    protected fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()

    // Block
    protected fun simpleBlockAndItem(block: HTHolderLike<Block, *>, model: ModelFile = cubeAll(block.get())) {
        simpleBlockWithItem(block.get(), model)
    }

    protected fun layeredBlock(block: HTHolderLike<Block, *>, layer0: ResourceLocation, layer1: ResourceLocation) {
        simpleBlockAndItem(
            block,
            models()
                .withExistingParent(block.getPath(), HiiragiCoreAPI.id("block", "layered"))
                .texture("layer0", layer0)
                .texture("layer1", layer1)
                .renderType("cutout"),
        )
    }

    protected fun horizontalBlock(block: HTHolderLike<Block, *>, model: ModelFile) {
        horizontalBlock(block.get(), model)
        itemModels().simpleBlockItem(block.get())
    }

    protected fun cubeColumn(
        block: HTHolderLike<Block, *>,
        side: ResourceLocation = block.blockId.withSuffix("_side"),
        end: ResourceLocation = block.blockId.withSuffix("_top"),
    ) {
        simpleBlockAndItem(block, models().cubeColumn(block.blockId.path, side, end))
    }

    protected fun altModelBlock(
        block: HTHolderLike<Block, *>,
        id: ResourceLocation = block.blockId,
        factory: (HTHolderLike<Block, *>, ModelFile) -> Unit = ::simpleBlockAndItem,
    ) {
        factory(block, ModelFile.ExistingModelFile(id, fileHelper))
    }

    protected fun altTextureBlock(block: HTHolderLike<Block, *>, all: ResourceLocation) {
        simpleBlockAndItem(block, models().cubeAll(block.getPath(), all))
    }

    protected fun cutoutSimpleBlock(block: HTHolderLike<Block, *>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.getPath(), texId).renderType("cutout"))
    }

    protected fun translucentSimpleBlock(block: HTHolderLike<Block, *>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.getPath(), texId).renderType("translucent"))
    }

    protected fun slabBlock(block: HTHolderLike<Block, SlabBlock>, texture: ResourceLocation) {
        slabBlock(block.get(), texture, texture)
        itemModels().simpleBlockItem(block.getId())
    }

    protected fun stairsBlock(block: HTHolderLike<Block, StairBlock>, texture: ResourceLocation) {
        stairsBlock(block.get(), texture)
        itemModels().simpleBlockItem(block.getId())
    }

    protected fun wallBlock(block: HTHolderLike<Block, WallBlock>, texture: ResourceLocation) {
        wallBlock(block.get(), texture)
        itemModels().wallInventory(block.getPath(), texture)
    }
}
