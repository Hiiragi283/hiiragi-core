@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.vanillaId
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[BlockStateProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTBlockStateProvider(fileHelper: ExistingFileHelper, output: PackOutput, protected val modid: String) : BlockStateProvider(output, modid, fileHelper) {
    //    Extensions    //

    /**
     * @since 0.10.0
     */
    protected fun trackBlock(id: HTIdLike) {
        models().trackTexture(id.blockId)
    }

    /**
     * @since 0.10.0
     */
    protected fun trackItem(id: HTIdLike) {
        models().trackTexture(id.itemId)
    }

    protected fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()

    // Block

    protected fun <BLOCK : Block> registerVariants(
        block: SupplierWithId<BLOCK>,
        stateDispatcher: (SupplierWithId<BLOCK>, BlockState) -> Array<ConfiguredModel>,
    ) {
        getVariantBuilder(block.get()).forAllStates(stateDispatcher.partially1(block))
    }

    /**
     * アイテム時に専用のレンダラーを使用するブロック向けのモデル
     * @since 0.15.0
     */
    protected val builtIn: ModelFile = models().getExistingFile(HiiragiCoreAPI.id(HTConst.BLOCK, "builtin"))

    /**
     * フルブロックのモデルを登録します。
     */
    protected fun simpleBlockAndItem(block: SupplierWithId<Block>, model: ModelFile = cubeAll(block.get()), itemModel: ModelFile = model) {
        simpleBlock(block.get(), model)
        simpleBlockItem(block.get(), itemModel)
    }

    /**
     * @since 0.14.0
     */
    protected fun simpleBlockAndItem(block: SupplierWithId<Block>, vararg models: ConfiguredModel, itemModel: ModelFile = models[0].model) {
        simpleBlock(block.get(), *models)
        simpleBlockItem(block.get(), itemModel)
    }

    /**
     * @since 0.14.0
     */
    protected fun <BLOCK : Block> simpleBlockAndItem(block: SupplierWithId<BLOCK>, factory: (SupplierWithId<BLOCK>) -> Array<ConfiguredModel>) {
        contract {
            callsInPlace(factory, InvocationKind.EXACTLY_ONCE)
        }
        simpleBlockAndItem(block, *factory(block))
    }

    /**
     * @since 0.15.0
     */
    protected fun <BLOCK : Block> simpleBlockAndItem(
        block: SupplierWithId<BLOCK>,
        factory: (SupplierWithId<BLOCK>) -> Array<ConfiguredModel>,
        itemFactory: (Array<ConfiguredModel>) -> ModelFile,
    ) {
        contract {
            callsInPlace(factory, InvocationKind.EXACTLY_ONCE)
            callsInPlace(itemFactory, InvocationKind.EXACTLY_ONCE)
        }
        val models: Array<ConfiguredModel> = factory(block)
        simpleBlockAndItem(block, *models, itemModel = itemFactory(models))
    }

    /**
     * レイヤーを持ったフルブロックのモデルを登録します。
     */
    protected fun layeredBlock(block: SupplierWithId<Block>, layer0: ResourceLocation, layer1: ResourceLocation) {
        simpleBlockAndItem(
            block,
            models()
                .withExistingParent(block, HiiragiCoreAPI.id(HTConst.BLOCK, "layered"))
                .texture("layer0", layer0)
                .texture("layer1", layer1)
                .renderType("cutout"),
        )
    }

    /**
     * 水平方向に回転できるモデルを登録します。
     */
    protected fun horizontalBlock(block: SupplierWithId<Block>, model: ModelFile) {
        horizontalBlock(block.get(), model)
        itemModels().simpleBlockItem(block.getId())
    }

    /**
     * 柱状のモデルを登録します。
     */
    protected fun cubeColumn(
        block: SupplierWithId<Block>,
        side: ResourceLocation = block.blockId.withSuffix("_side"),
        end: ResourceLocation = block.blockId.withSuffix("_top"),
    ) {
        simpleBlockAndItem(block, models().cubeColumn(block.blockId.path, side, end))
    }

    /**
     * テクスチャに[all]を使用するフルブロックのモデルを登録します。
     */
    protected fun altTextureBlock(block: SupplierWithId<Block>, all: ResourceLocation) {
        simpleBlockAndItem(block, models().cubeAll(block.path, all))
    }

    /**
     * 描画タイプが`cutout`となるフルブロックのモデルを登録します。
     */
    protected fun cutoutSimpleBlock(block: SupplierWithId<Block>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.path, texId).renderType("cutout"))
    }

    /**
     * 描画タイプが`translucent`となるフルブロックのモデルを登録します。
     */
    protected fun translucentSimpleBlock(block: SupplierWithId<Block>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.path, texId).renderType("translucent"))
    }

    /**
     * ハーフブロックのモデルを登録します。
     */
    protected fun slabBlock(block: SupplierWithId<SlabBlock>, texture: ResourceLocation) {
        slabBlock(block.get(), texture, texture)
        itemModels().simpleBlockItem(block.getId())
    }

    /**
     * 階段ブロックのモデルを登録します。
     */
    protected fun stairsBlock(block: SupplierWithId<StairBlock>, texture: ResourceLocation) {
        stairsBlock(block.get(), texture)
        itemModels().simpleBlockItem(block.getId())
    }

    /**
     * 壁ブロックのモデルを登録します。
     */
    protected fun wallBlock(block: SupplierWithId<WallBlock>, texture: ResourceLocation) {
        wallBlock(block.get(), texture)
        itemModels().wallInventory(block.path, texture)
    }

    /**
     * 液体ブロックのモデルを追加します。
     * @since 0.3.0
     */
    protected fun liquidBlock(content: HTFluidContent.Flowing) {
        val block: Block = content.blockHolder?.get() ?: return
        simpleBlock(
            block,
            models()
                .getBuilder(content.blockId)
                .texture("particle", vanillaId(HTConst.BLOCK, "water_still")),
        )
    }
}
