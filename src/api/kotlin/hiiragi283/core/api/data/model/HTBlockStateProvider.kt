package hiiragi283.core.api.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.toId
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

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[BlockStateProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTBlockStateProvider(protected val modId: String, context: HTDataGenContext) :
    BlockStateProvider(context.output, modId, context.fileHelper) {
    protected val fileHelper: ExistingFileHelper = context.fileHelper

    //    Extensions    //

    /**
     * 指定した[ID][id]でモデルのビルダーを作成します。
     */
    protected fun <BUILDER : ModelBuilder<BUILDER>, PROVIDER : ModelProvider<BUILDER>> PROVIDER.getBuilder(id: ResourceLocation): BUILDER =
        this.getBuilder(id.toString())

    /**
     * 指定した[like]からモデルのビルダーを作成します。
     */
    protected fun <BUILDER : ModelBuilder<BUILDER>, PROVIDER : ModelProvider<BUILDER>> PROVIDER.getBuilder(like: HTIdLike): BUILDER =
        this.getBuilder(like.getId())

    /**
     * 指定したテクスチャが存在する場合にのみモデルを登録します。
     * @param block モデルを登録させるブロック
     * @param action モデルを登録するブロック
     */
    protected inline fun existTexture(block: HTBlockHolderLike<*, *>, action: (HTBlockHolderLike<*, *>) -> Unit) {
        existTexture(block, block.blockId) { blockIn: HTBlockHolderLike<*, *>, _: ResourceLocation -> action(blockIn) }
    }

    protected inline fun existTexture(
        block: HTBlockHolderLike<*, *>,
        id: ResourceLocation,
        action: (HTBlockHolderLike<*, *>, ResourceLocation) -> Unit,
    ) {
        if (fileHelper.exists(id, TEXTURE)) {
            action(block, id)
        } else {
            HiiragiCoreAPI.LOGGER.debug("Missing texture {} for {}", id, block.getId())
        }
    }

    protected fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()

    // Block

    /**
     * フルブロックのモデルを登録します。
     */
    protected fun simpleBlockAndItem(block: HTBlockHolderLike<*, *>, model: ModelFile = cubeAll(block.asBlock())) {
        simpleBlockWithItem(block.asBlock(), model)
    }

    /**
     * レイヤーを持ったフルブロックのモデルを登録します。
     */
    protected fun layeredBlock(block: HTBlockHolderLike<*, *>, layer0: ResourceLocation, layer1: ResourceLocation) {
        simpleBlockAndItem(
            block,
            models()
                .withExistingParent(block.path, HiiragiCoreAPI.id(HTConst.BLOCK, "layered"))
                .texture("layer0", layer0)
                .texture("layer1", layer1)
                .renderType("cutout"),
        )
    }

    /**
     * 水平方向に回転できるモデルを登録します。
     */
    protected fun horizontalBlock(block: HTBlockHolderLike<*, *>, model: ModelFile) {
        horizontalBlock(block.asBlock(), model)
        itemModels().simpleBlockItem(block.asBlock())
    }

    /**
     * 柱状のモデルを登録します。
     */
    protected fun cubeColumn(
        block: HTBlockHolderLike<*, *>,
        side: ResourceLocation = block.blockId.withSuffix("_side"),
        end: ResourceLocation = block.blockId.withSuffix("_top"),
    ) {
        simpleBlockAndItem(block, models().cubeColumn(block.blockId.path, side, end))
    }

    /**
     * 既存のモデルを使用して登録します。
     */
    protected fun altModelBlock(
        block: HTBlockHolderLike<*, *>,
        id: ResourceLocation = block.blockId,
        factory: (HTBlockHolderLike<*, *>, ModelFile) -> Unit = ::simpleBlockAndItem,
    ) {
        factory(block, ModelFile.ExistingModelFile(id, fileHelper))
    }

    /**
     * テクスチャに[all]を使用するフルブロックのモデルを登録します。
     */
    protected fun altTextureBlock(block: HTBlockHolderLike<*, *>, all: ResourceLocation) {
        simpleBlockAndItem(block, models().cubeAll(block.path, all))
    }

    /**
     * 描画タイプが`cutout`となるフルブロックのモデルを登録します。
     */
    protected fun cutoutSimpleBlock(block: HTBlockHolderLike<*, *>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.path, texId).renderType("cutout"))
    }

    /**
     * 描画タイプが`translucent`となるフルブロックのモデルを登録します。
     */
    protected fun translucentSimpleBlock(block: HTBlockHolderLike<*, *>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.path, texId).renderType("translucent"))
    }

    /**
     * ハーフブロックのモデルを登録します。
     */
    protected fun slabBlock(block: HTBlockHolderLike<SlabBlock, *>, texture: ResourceLocation) {
        slabBlock(block.asBlock(), texture, texture)
        itemModels().simpleBlockItem(block.getId())
    }

    /**
     * 階段ブロックのモデルを登録します。
     */
    protected fun stairsBlock(block: HTBlockHolderLike<StairBlock, *>, texture: ResourceLocation) {
        stairsBlock(block.asBlock(), texture)
        itemModels().simpleBlockItem(block.getId())
    }

    /**
     * 壁ブロックのモデルを登録します。
     */
    protected fun wallBlock(block: HTBlockHolderLike<WallBlock, *>, texture: ResourceLocation) {
        wallBlock(block.asBlock(), texture)
        itemModels().wallInventory(block.path, texture)
    }

    /**
     * 液体ブロックのモデルを追加します。
     * @since 0.3.0
     */
    protected fun liquidBlock(content: HTFluidContent) {
        val block: Block = content.blockHolder?.asBlock() ?: return
        simpleBlock(
            block,
            models()
                .getBuilder(content.blockId)
                .texture("particle", HTConst.MINECRAFT.toId(HTConst.BLOCK, "water_still")),
        )
    }
}
