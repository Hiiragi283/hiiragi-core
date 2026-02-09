package hiiragi283.core.api.data.model

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.IdToFunction
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.blockId
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import net.mehvahdjukaar.moonlight.api.resources.ResType
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
import net.minecraft.data.models.model.TextureSlot
import net.minecraft.data.models.model.TexturedModel
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.level.block.state.properties.Property
import java.util.function.Supplier

abstract class HTModelProvider : ResourceGenTask {
    private lateinit var sink: ResourceSink

    final override fun accept(manager: ResourceManager, sink: ResourceSink) {
        this.sink = sink
        registerModels(manager)
    }

    protected abstract fun registerModels(manager: ResourceManager)

    //    Extensions    //

    protected val modelOutput: (
        ResourceLocation,
        Supplier<JsonElement>,
    ) -> Unit = { id: ResourceLocation, json: Supplier<JsonElement> ->
        sink.addJson(id, json.get(), ResType.MODELS)
    }

    // Block

    /**
     * @see net.minecraft.data.models.ModelProvider.run
     */
    protected fun addBlockState(generator: BlockStateGenerator, block: HTIdLike) {
        sink.addBlockState(block.getId(), generator.get())
    }

    /**
     * @see net.minecraft.data.models.BlockModelGenerators.createTrivialBlock
     */
    protected fun addSimpleBlock(block: HTBlockHolderLike<*, *>, provider: TexturedModel.Provider = TexturedModel.CUBE) {
        addBlockState(createSimpleGenerator(block, provider.create(block.asBlock(), modelOutput)), block)
    }

    protected fun addSimpleBlockAndItem(block: HTBlockHolderLike<*, *>, provider: TexturedModel.Provider = TexturedModel.CUBE) {
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
            TexturedModel.PARTICLE_ONLY.updateTexture {
                it.put(TextureSlot.PARTICLE, HTConst.MINECRAFT.toId(HTConst.BLOCK, "water_still"))
            },
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
        val id: ResourceLocation = block.blockId.withSuffix(suffix)
        return model.create(id, textureFactory.apply(id), modelOutput)
    }

    protected fun addBlockModel(block: HTIdLike, model: ModelTemplate, textureMap: TextureMapping): ResourceLocation =
        model.create(block.blockId, textureMap, modelOutput)

    // Item
    protected fun addItemModel(item: HTIdLike, model: ModelTemplate, textureMap: TextureMapping) {
        model.create(item.itemId, textureMap, modelOutput)
    }

    protected fun addSimpleItemModel(item: HTIdLike) {
        addItemModel(item, ModelTemplates.FLAT_ITEM, TextureMapping.layer0(item.itemId))
    }

    protected fun addLayeredItemModel(item: HTIdLike, vararg layers: ResourceLocation) {
        addLayeredItemModel(item, listOf(*layers))
    }

    protected fun addLayeredItemModel(item: HTIdLike, layers: List<ResourceLocation>) {
        when (layers.size) {
            1 -> addItemModel(item, ModelTemplates.FLAT_ITEM, TextureMapping.layer0(layers[0]))
            2 -> addItemModel(item, ModelTemplates.TWO_LAYERED_ITEM, TextureMapping.layered(layers[0], layers[1]))
            3 -> addItemModel(item, ModelTemplates.THREE_LAYERED_ITEM, TextureMapping.layered(layers[0], layers[1], layers[2]))
            else -> error("Unsupported layer count: ${layers.size}")
        }
    }

    /**
     * @see net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder
     */
    protected fun addBucketModel(content: HTFluidContent, isDrip: Boolean) {
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
        sink.addItemModel(content.bucketHolder.getId(), root)
    }
}
