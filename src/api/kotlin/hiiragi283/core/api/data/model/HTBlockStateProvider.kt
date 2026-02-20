package hiiragi283.core.api.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
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
     * @since 0.10.0
     */
    protected fun exists(id: ResourceLocation): Boolean = this.fileHelper.exists(id, TEXTURE)

    /**
     * @since 0.10.0
     */
    protected fun track(id: ResourceLocation) {
        this.fileHelper.trackGenerated(id, TEXTURE)
    }

    /**
     * @since 0.10.0
     */
    protected fun trackBlock(id: HTIdLike) {
        this.track(id.blockId)
    }

    /**
     * @since 0.10.0
     */
    protected fun trackItem(id: HTIdLike) {
        this.track(id.itemId)
    }

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

    protected fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()

    // Block

    protected fun <BLOCK : HTBlockHolderLike<*>> registerVariants(
        block: BLOCK,
        stateDispatcher: (BLOCK, BlockState) -> Array<ConfiguredModel>,
    ) {
        getVariantBuilder(block.get()).forAllStates(stateDispatcher.partially1(block))
    }

    /**
     * フルブロックのモデルを登録します。
     */
    protected fun simpleBlockAndItem(block: HTBlockHolderLike<*>, model: ModelFile = cubeAll(block.get())) {
        simpleBlockWithItem(block.get(), model)
    }

    protected fun <BLOCK : HTBlockHolderLike<*>> simpleBlockAndItem(block: BLOCK, factory: (BLOCK) -> ModelFile) {
        simpleBlockWithItem(block.get(), factory(block))
    }

    /**
     * レイヤーを持ったフルブロックのモデルを登録します。
     */
    protected fun layeredBlock(block: HTBlockHolderLike<*>, layer0: ResourceLocation, layer1: ResourceLocation) {
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
    protected fun horizontalBlock(block: HTBlockHolderLike<*>, model: ModelFile) {
        horizontalBlock(block.get(), model)
        itemModels().simpleBlockItem(block.get())
    }

    /**
     * 柱状のモデルを登録します。
     */
    protected fun cubeColumn(
        block: HTBlockHolderLike<*>,
        side: ResourceLocation = block.blockId.withSuffix("_side"),
        end: ResourceLocation = block.blockId.withSuffix("_top"),
    ) {
        simpleBlockAndItem(block, models().cubeColumn(block.blockId.path, side, end))
    }

    /**
     * 既存のモデルを使用して登録します。
     */
    protected fun <BLOCK : HTBlockHolderLike<*>> altModelBlock(
        block: BLOCK,
        id: ResourceLocation = block.blockId,
        factory: (BLOCK, ModelFile) -> Unit = ::simpleBlockAndItem,
    ) {
        factory(block, ModelFile.ExistingModelFile(id, fileHelper))
    }

    /**
     * テクスチャに[all]を使用するフルブロックのモデルを登録します。
     */
    protected fun altTextureBlock(block: HTBlockHolderLike<*>, all: ResourceLocation) {
        simpleBlockAndItem(block, models().cubeAll(block.path, all))
    }

    /**
     * 描画タイプが`cutout`となるフルブロックのモデルを登録します。
     */
    protected fun cutoutSimpleBlock(block: HTBlockHolderLike<*>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.path, texId).renderType("cutout"))
    }

    /**
     * 描画タイプが`translucent`となるフルブロックのモデルを登録します。
     */
    protected fun translucentSimpleBlock(block: HTBlockHolderLike<*>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.path, texId).renderType("translucent"))
    }

    /**
     * ハーフブロックのモデルを登録します。
     */
    protected fun slabBlock(block: HTBlockHolderLike<out SlabBlock>, texture: ResourceLocation) {
        slabBlock(block.get(), texture, texture)
        itemModels().simpleBlockItem(block.getId())
    }

    /**
     * 階段ブロックのモデルを登録します。
     */
    protected fun stairsBlock(block: HTBlockHolderLike<out StairBlock>, texture: ResourceLocation) {
        stairsBlock(block.get(), texture)
        itemModels().simpleBlockItem(block.getId())
    }

    /**
     * 壁ブロックのモデルを登録します。
     */
    protected fun wallBlock(block: HTBlockHolderLike<out WallBlock>, texture: ResourceLocation) {
        wallBlock(block.get(), texture)
        itemModels().wallInventory(block.path, texture)
    }

    /**
     * 液体ブロックのモデルを追加します。
     * @since 0.3.0
     */
    protected fun liquidBlock(content: HTFluidContent) {
        val block: Block = content.blockHolder?.get() ?: return
        simpleBlock(
            block,
            models()
                .getBuilder(content.blockId)
                .texture("particle", HTConst.MINECRAFT.toId(HTConst.BLOCK, "water_still")),
        )
    }
}
