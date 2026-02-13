package hiiragi283.core.api.data.model

import com.google.gson.JsonObject
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.IdToFunction
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.toId
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.data.models.blockstates.MultiVariantGenerator
import net.minecraft.data.models.blockstates.PropertyDispatch
import net.minecraft.data.models.blockstates.Variant
import net.minecraft.data.models.blockstates.VariantProperties
import net.minecraft.data.models.model.DelegatedModel
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.level.block.state.properties.Property

/**
 * BlockState JSONおよびモデルJSONを生成する[ResourceGenTask]の抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
abstract class HTModelProvider : ResourceGenTask {
    protected lateinit var sink: ResourceSink
        private set

    final override fun accept(manager: ResourceManager, sink: ResourceSink) {
        this.sink = sink
        registerModels(manager)
    }

    /**
     * BlockStateやモデルを生成します。
     */
    protected abstract fun registerModels(manager: ResourceManager)

    //    Extensions    //

    /**
     * モデルJSONの出力先のインスタンス
     */
    protected val modelOutput: HTModelOutput = HTModelOutput { id, json, type -> sink.addJson(id, json, type) }

    // Block

    /**
     * BlockState JSONを生成します。
     * @see net.minecraft.data.models.ModelProvider.run
     */
    protected fun addBlockState(generator: BlockStateGenerator, block: HTIdLike) {
        sink.addBlockState(block.getId(), generator.get())
    }

    protected fun addSimpleBlock(block: HTBlockHolderLike<*, *>, model: HTTexturedModel) {
        addBlockState(createSimpleGenerator(block, model.saveBlock(block, modelOutput)), block)
    }

    protected fun addSimpleBlockAndItem(block: HTBlockHolderLike<*, *>, model: HTTexturedModel) {
        addSimpleBlock(block, model)
        sink.addItemModel(block.getId(), DelegatedModel(block.blockId).get())
    }

    /**
     * @see net.minecraft.data.models.BlockModelGenerators.createTrivialBlock
     */
    protected fun addSimpleBlock(block: HTBlockHolderLike<*, *>, provider: HTTexturedModel.Provider = HTTexturedModels.CUBE_ALL) {
        addBlockState(createSimpleGenerator(block, provider.saveBlock(block, modelOutput)), block)
    }

    protected fun addSimpleBlockAndItem(block: HTBlockHolderLike<*, *>, provider: HTTexturedModel.Provider = HTTexturedModels.CUBE_ALL) {
        addSimpleBlock(block, provider)
        sink.addItemModel(block.getId(), DelegatedModel(block.blockId).get())
    }

    /**
     * @see net.minecraft.data.models.BlockModelGenerators.createSimpleBlock
     */
    protected fun createSimpleGenerator(block: HTBlockHolderLike<*, *>, modelId: ResourceLocation): MultiVariantGenerator =
        MultiVariantGenerator.multiVariant(block.asBlock(), Variant.variant().with(VariantProperties.MODEL, modelId))

    protected fun addLiquidBlock(content: HTFluidContent) {
        val block: HTBlockHolderLike<*, *> = content.blockHolder ?: return
        addSimpleBlock(
            block,
            HTTexturedModels.particleOnly(HTConst.MINECRAFT.toId(HTConst.BLOCK, "water_still")),
        )
    }

    /**
     * @see net.minecraft.data.models.BlockModelGenerators.createCropBlock
     */
    protected fun addCropBlock(block: HTBlockHolderLike<*, *>, ageProperty: Property<Int>, vararg ageToSuffix: Int) {
        // Block
        require(ageProperty.possibleValues.size == ageToSuffix.size)
        val map: MutableMap<Int, ResourceLocation> = hashMapOf()
        addBlockState(
            MultiVariantGenerator
                .multiVariant(block.asBlock())
                .with(
                    PropertyDispatch
                        .property(ageProperty)
                        .generate { age: Int ->
                            val suffix: Int = ageToSuffix[age]
                            val modelId: ResourceLocation = map.computeIfAbsent(suffix) {
                                addBlockModel(
                                    block,
                                    "_stage$it",
                                    ModelTemplates.CROP,
                                    TextureMapping::crop,
                                )
                            }
                            Variant.variant().with(VariantProperties.MODEL, modelId)
                        },
                ),
            block,
        )
        // Item
        addSimpleItemModel(block)
    }

    /**
     * @see net.minecraft.data.models.BlockModelGenerators.createSuffixedVariant
     */
    protected fun addBlockModel(
        block: HTIdLike,
        suffix: String,
        model: ModelTemplate,
        textureFactory: IdToFunction<TextureMapping>,
    ): ResourceLocation {
        val id: ResourceLocation = block.blockId
        return model.create(id.withSuffix(suffix), textureFactory.apply(id), modelOutput)
    }

    protected fun addBlockModel(block: HTIdLike, provider: HTTexturedModel.Provider): ResourceLocation =
        provider.saveBlock(block, modelOutput)

    // Item
    protected fun addItemModel(item: HTIdLike, model: HTTexturedModel) {
        model.saveItem(item, modelOutput)
    }

    protected fun addItemModel(item: HTIdLike, provider: HTTexturedModel.Provider) {
        provider.saveItem(item, modelOutput)
    }

    protected fun addSimpleItemModel(item: HTIdLike) {
        addItemModel(item, HTTexturedModels.FLAT_ITEM)
    }

    /**
     * @see net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder
     */
    protected fun addBucketModel(content: HTFluidHolderLike<*>, isDrip: Boolean) {
        val parent: ResourceLocation = when {
            isDrip -> "bucket_drip"
            else -> "bucket"
        }.let { HTConst.NEOFORGE.toId(HTConst.ITEM, it) }
        val root = JsonObject()
        root.addProperty("parent", parent.toString())
        root.addProperty("fluid", content.getId().toString())
        root.addProperty("loader", "neoforge:fluid_container")
        if (content.getFluidType().isLighterThanAir) {
            root.addProperty("flip_gas", "true")
        }
        sink.addItemModel(content.getBucketHolder().getId(), root)
    }
}
