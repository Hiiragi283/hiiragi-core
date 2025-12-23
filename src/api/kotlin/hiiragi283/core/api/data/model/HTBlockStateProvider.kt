package hiiragi283.core.api.data.model

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.vanillaId
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
import org.slf4j.Logger

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[BlockStateProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTBlockStateProvider(modid: String, context: HTDataGenContext) :
    BlockStateProvider(context.output, modid, context.fileHelper) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

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
    protected inline fun existTexture(block: HTHolderLike<Block, *>, action: (HTHolderLike<Block, *>) -> Unit) {
        existTexture(block, block.blockId) { blockIn: HTHolderLike<Block, *>, _: ResourceLocation -> action(blockIn) }
    }

    protected inline fun existTexture(
        block: HTHolderLike<Block, *>,
        id: ResourceLocation,
        action: (HTHolderLike<Block, *>, ResourceLocation) -> Unit,
    ) {
        if (fileHelper.exists(id, TEXTURE)) {
            action(block, id)
        } else {
            LOGGER.debug("Missing texture {} for {}", id, block.getId())
        }
    }

    protected fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()

    // Block

    /**
     * フルブロックのモデルを登録します。
     */
    protected fun simpleBlockAndItem(block: HTHolderLike<Block, *>, model: ModelFile = cubeAll(block.get())) {
        simpleBlockWithItem(block.get(), model)
    }

    /**
     * レイヤーを持ったフルブロックのモデルを登録します。
     */
    protected fun layeredBlock(block: HTHolderLike<Block, *>, layer0: ResourceLocation, layer1: ResourceLocation) {
        simpleBlockAndItem(
            block,
            models()
                .withExistingParent(block.getPath(), HiiragiCoreAPI.id(HTConst.BLOCK, "layered"))
                .texture("layer0", layer0)
                .texture("layer1", layer1)
                .renderType("cutout"),
        )
    }

    /**
     * 水平方向に回転できるモデルを登録します。
     */
    protected fun horizontalBlock(block: HTHolderLike<Block, *>, model: ModelFile) {
        horizontalBlock(block.get(), model)
        itemModels().simpleBlockItem(block.get())
    }

    /**
     * 柱状のモデルを登録します。
     */
    protected fun cubeColumn(
        block: HTHolderLike<Block, *>,
        side: ResourceLocation = block.blockId.withSuffix("_side"),
        end: ResourceLocation = block.blockId.withSuffix("_top"),
    ) {
        simpleBlockAndItem(block, models().cubeColumn(block.blockId.path, side, end))
    }

    /**
     * 既存のモデルを使用して登録します。
     */
    protected fun altModelBlock(
        block: HTHolderLike<Block, *>,
        id: ResourceLocation = block.blockId,
        factory: (HTHolderLike<Block, *>, ModelFile) -> Unit = ::simpleBlockAndItem,
    ) {
        factory(block, ModelFile.ExistingModelFile(id, fileHelper))
    }

    /**
     * テクスチャに[all]を使用するフルブロックのモデルを登録します。
     */
    protected fun altTextureBlock(block: HTHolderLike<Block, *>, all: ResourceLocation) {
        simpleBlockAndItem(block, models().cubeAll(block.getPath(), all))
    }

    /**
     * 描画タイプが`cutout`となるフルブロックのモデルを登録します。
     */
    protected fun cutoutSimpleBlock(block: HTHolderLike<Block, *>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.getPath(), texId).renderType("cutout"))
    }

    /**
     * 描画タイプが`translucent`となるフルブロックのモデルを登録します。
     */
    protected fun translucentSimpleBlock(block: HTHolderLike<Block, *>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.getPath(), texId).renderType("translucent"))
    }

    /**
     * ハーフブロックのモデルを登録します。
     */
    protected fun slabBlock(block: HTHolderLike<Block, SlabBlock>, texture: ResourceLocation) {
        slabBlock(block.get(), texture, texture)
        itemModels().simpleBlockItem(block.getId())
    }

    /**
     * 階段ブロックのモデルを登録します。
     */
    protected fun stairsBlock(block: HTHolderLike<Block, StairBlock>, texture: ResourceLocation) {
        stairsBlock(block.get(), texture)
        itemModels().simpleBlockItem(block.getId())
    }

    /**
     * 壁ブロックのモデルを登録します。
     */
    protected fun wallBlock(block: HTHolderLike<Block, WallBlock>, texture: ResourceLocation) {
        wallBlock(block.get(), texture)
        itemModels().wallInventory(block.getPath(), texture)
    }

    /**
     * 液体ブロックのモデルを追加します。
     * @since 0.3.0
     */
    protected fun liquidBlock(content: HTFluidContent<*, *, *>) {
        simpleBlock(
            content.block.get(),
            models()
                .getBuilder(content.blockId)
                .texture("particle", vanillaId(HTConst.BLOCK, "water_still")),
        )
    }
}
