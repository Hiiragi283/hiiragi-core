package hiiragi283.core.common.registry.register

import hiiragi283.core.api.registry.BlockWithContextFactory
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.ItemWithContextFactory
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.common.registry.HTDeferredItem
import hiiragi283.core.common.registry.HTDeferredOnlyBlock
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DispenserBlock
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.PushReaction
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.fluids.BaseFlowingFluid
import net.neoforged.neoforge.fluids.DispenseFluidContainer
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.UnaryOperator

typealias HTSimpleFluidContent = HTFluidContent.Flowing<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, BucketItem>

class HTFluidContentRegister(modId: String) {
    private val fluidRegister: HTDeferredRegister<Fluid> = HTDeferredRegister(Registries.FLUID, modId)
    private val typeRegister: HTDeferredRegister<FluidType> = HTDeferredRegister(NeoForgeRegistries.Keys.FLUID_TYPES, modId)
    private val blockRegister = HTDeferredOnlyBlockRegister(modId)
    private val itemRegister = HTDeferredItemRegister(modId)

    fun asFluidSequence(): Sequence<HTDeferredHolder<Fluid, *>> = fluidRegister.asSequence()

    fun asTypeSequence(): Sequence<HTDeferredHolder<FluidType, *>> = typeRegister.asSequence()

    fun asBlockSequence(): Sequence<HTDeferredOnlyBlock<*>> = blockRegister.asSequence()

    fun asItemSequence(): Sequence<HTDeferredItem<*>> = itemRegister.asSequence()

    private val contentsCache: MutableMap<ResourceKey<Fluid>, HTFluidContent<*, *, *>> = mutableMapOf()
    val keys: Set<ResourceKey<Fluid>> get() = contentsCache.keys
    val entries: Collection<HTFluidContent<*, *, *>> get() = contentsCache.values

    operator fun get(key: ResourceKey<Fluid>): HTFluidContent<*, *, *>? = contentsCache[key]

    fun asSequence(): Sequence<HTFluidContent<*, *, *>> = entries.asSequence()

    fun addAlias(from: String, to: String) {
        typeRegister.addAlias(from, to)

        fluidRegister.addAlias(from, to)
        fluidRegister.addAlias("flowing_$from", "flowing_$to")

        blockRegister.addAlias(from, to)

        itemRegister.addAlias("${from}_bucket", "${to}_bucket")
    }

    fun register(eventBus: IEventBus) {
        fluidRegister.register(eventBus)
        typeRegister.register(eventBus)
        blockRegister.register(eventBus)
        itemRegister.register(eventBus)

        eventBus.addListener { event: FMLCommonSetupEvent ->
            event.enqueueWork {
                for (item: HTDeferredItem<*> in asItemSequence()) {
                    DispenserBlock.registerBehavior(item, DispenseFluidContainer.getInstance())
                }
            }
        }
    }

    //    Content    //

    fun <TYPE : FluidType, FLUID : Fluid, ITEM : Item> register(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> TYPE,
        fluidFactory: () -> FLUID,
        bucketFactory: ItemWithContextFactory<FLUID, ITEM>,
    ): HTFluidContent<TYPE, FLUID, ITEM> {
        // Fluid Type
        val typeHolder: HTDeferredHolder<FluidType, TYPE> = typeRegister.register(name) { _ ->
            typeFactory(properties.descriptionId("block.${typeRegister.namespace}.$name"))
        }
        // Fluid
        val fluidHolder: HTDeferredHolder<Fluid, FLUID> = fluidRegister.register(name, fluidFactory)
        // Bucket Item
        val bucketHolder: HTDeferredItem<ITEM> = itemRegister.registerItem(
            "${name}_bucket",
            { bucketFactory(fluidHolder.get(), it) },
            { it.stacksTo(1).craftRemainder(Items.BUCKET) },
        )
        // Contents
        val content: ContentImpl<TYPE, FLUID, ITEM> = ContentImpl(
            typeHolder,
            fluidHolder,
            Registries.FLUID.createCommonTag(name),
            bucketHolder,
            Registries.ITEM.createCommonTag("buckets", name),
        )
        contentsCache[fluidHolder.key] = content
        return content
    }

    private class ContentImpl<TYPE : FluidType, FLUID : Fluid, ITEM : Item>(
        override val typeHolder: HTDeferredHolder<FluidType, TYPE>,
        fluidHolder: HTDeferredHolder<Fluid, FLUID>,
        override val fluidTag: TagKey<Fluid>,
        override val bucketHolder: HTItemHolderLike<ITEM>,
        override val bucketTag: TagKey<Item>,
    ) : HTFluidContent<TYPE, FLUID, ITEM>,
        HTHolderLike.HolderDelegate<Fluid, FLUID> by fluidHolder

    //    Flowing    //

    fun registerSimpleFlowing(
        name: String,
        properties: FluidType.Properties,
        blockFactory: BlockWithContextFactory<BaseFlowingFluid.Source, LiquidBlock> = ::LiquidBlock,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTSimpleFluidContent = registerFlowing(name, properties, ::BucketItem, blockFactory, blockProperties)

    fun <ITEM : Item> registerFlowing(
        name: String,
        properties: FluidType.Properties,
        bucketFactory: ItemWithContextFactory<BaseFlowingFluid.Source, ITEM>,
        blockFactory: BlockWithContextFactory<BaseFlowingFluid.Source, LiquidBlock> = ::LiquidBlock,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTFluidContent.Flowing<FluidType, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, ITEM> =
        registerFlowing(name, properties, ::FluidType, bucketFactory, blockFactory, blockProperties)

    fun <TYPE : FluidType> registerFlowing(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> TYPE,
        blockFactory: BlockWithContextFactory<BaseFlowingFluid.Source, LiquidBlock> = ::LiquidBlock,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTFluidContent.Flowing<TYPE, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, BucketItem> =
        registerFlowing(name, properties, typeFactory, ::BucketItem, blockFactory, blockProperties)

    fun <TYPE : FluidType, ITEM : Item> registerFlowing(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> TYPE,
        bucketFactory: ItemWithContextFactory<BaseFlowingFluid.Source, ITEM>,
        blockFactory: BlockWithContextFactory<BaseFlowingFluid.Source, LiquidBlock> = ::LiquidBlock,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTFluidContent.Flowing<TYPE, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, ITEM> {
        // Fluid Type
        val typeHolder: HTDeferredHolder<FluidType, TYPE> = typeRegister.register(name) { _ ->
            typeFactory(properties.descriptionId("block.${typeRegister.namespace}.$name"))
        }
        // Fluid Holder
        val stillHolder: HTDeferredHolder<Fluid, BaseFlowingFluid.Source> =
            HTDeferredHolder(Registries.FLUID, fluidRegister.createId(name))
        val flowingHolder: HTDeferredHolder<Fluid, BaseFlowingFluid.Flowing> =
            HTDeferredHolder(Registries.FLUID, fluidRegister.createId("flowing_$name"))

        // Bucket Item
        val bucketHolder: HTDeferredItem<ITEM> = itemRegister.registerItem(
            "${name}_bucket",
            { bucketFactory(stillHolder.get(), it) },
            { it.stacksTo(1).craftRemainder(Items.BUCKET) },
        )
        // Liquid Block
        val blockHolder: HTDeferredOnlyBlock<LiquidBlock> = blockRegister.registerBlock(
            name,
            BlockBehaviour.Properties
                .of()
                .apply(blockProperties::apply)
                .noCollission()
                .strength(100f)
                .noLootTable()
                .replaceable()
                .pushReaction(PushReaction.DESTROY)
                .liquid(),
        ) { prop: BlockBehaviour.Properties -> blockFactory(stillHolder.get(), prop) }
        // Fluid
        val fluidProperties: BaseFlowingFluid.Properties = BaseFlowingFluid
            .Properties(typeHolder, stillHolder, flowingHolder)
            .block(blockHolder)
            .bucket(bucketHolder)
        val stillPath: String = stillHolder.getPath()
        fluidRegister.register(stillPath) { _ -> BaseFlowingFluid.Source(fluidProperties) }
        fluidRegister.register(flowingHolder.getPath()) { _ -> BaseFlowingFluid.Flowing(fluidProperties) }
        // Contents
        val content: FlowingImpl<TYPE, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing, ITEM> = FlowingImpl(
            typeHolder,
            stillHolder,
            flowingHolder,
            Registries.FLUID.createCommonTag(stillPath),
            blockHolder,
            bucketHolder,
            Registries.ITEM.createCommonTag("buckets", stillPath),
        )
        contentsCache[stillHolder.key] = content
        return content
    }

    private class FlowingImpl<TYPE : FluidType, STILL : Fluid, FLOWING : Fluid, ITEM : Item>(
        override val typeHolder: HTDeferredHolder<FluidType, TYPE>,
        fluidHolder: HTDeferredHolder<Fluid, STILL>,
        override val flowingHolder: HTDeferredHolder<Fluid, FLOWING>,
        override val fluidTag: TagKey<Fluid>,
        override val blockHolder: HTDeferredHolder<Block, out LiquidBlock>,
        override val bucketHolder: HTItemHolderLike<ITEM>,
        override val bucketTag: TagKey<Item>,
    ) : HTFluidContent.Flowing<TYPE, STILL, FLOWING, ITEM>,
        HTHolderLike.HolderDelegate<Fluid, STILL> by fluidHolder
}
