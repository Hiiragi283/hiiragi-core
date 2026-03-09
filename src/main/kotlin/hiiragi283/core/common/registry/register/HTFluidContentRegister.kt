package hiiragi283.core.common.registry.register

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.fluid.HTVirtualFluid
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.function.partially2
import hiiragi283.core.api.registry.BlockWithContextFactory
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.HTSimpleItemHolderLike
import hiiragi283.core.api.registry.ItemWithContextFactory
import hiiragi283.core.api.registry.addAlias
import hiiragi283.core.api.registry.asSequence
import hiiragi283.core.api.registry.createId
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.createCommonTag
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
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
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTFluidContentRegister(modId: String) {
    private val fluidRegister: DeferredRegister<Fluid> = DeferredRegister.create(Registries.FLUID, modId)
    private val typeRegister: DeferredRegister<FluidType> = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, modId)
    private val blockRegister = HTDeferredBlockRegister(modId)
    private val itemRegister = HTDeferredItemRegister(modId)

    fun asFluidSequence(): Sequence<HTFluidHolderLike<*>> = fluidRegister.asSequence()

    fun asTypeSequence(): Sequence<HTHolderLike.HolderDelegate<FluidType, *>> = typeRegister.asSequence()

    fun asBlockSequence(): Sequence<HTBlockHolderLike<*>> = blockRegister.asBlockSequence()

    fun asItemSequence(): Sequence<HTItemHolderLike<*>> = itemRegister.asItemSequence()

    private val contentsCache: MutableMap<ResourceKey<Fluid>, HTFluidContent> = mutableMapOf()
    val keys: Set<ResourceKey<Fluid>> get() = contentsCache.keys
    val entries: Collection<HTFluidContent> get() = contentsCache.values

    operator fun get(key: ResourceKey<Fluid>): HTFluidContent? = contentsCache[key]

    fun asSequence(): Sequence<HTFluidContent> = entries.asSequence()

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
                for (item: HTItemHolderLike<*> in asItemSequence()) {
                    DispenserBlock.registerBehavior(item, DispenseFluidContainer.getInstance())
                }
            }
        }
    }

    @HTBuilderMarker
    inline fun registerVirtual(name: String, builderAction: VirtualBuilder.() -> Unit): HTFluidContent =
        VirtualBuilder(name).apply(builderAction).build()

    @HTBuilderMarker
    inline fun registerFlowing(name: String, builderAction: FlowingBuilder.() -> Unit): HTFluidContent =
        FlowingBuilder(name).apply(builderAction).build()

    //    Builder    //

    abstract inner class Builder<FLUID : Fluid>(protected val name: String) {
        // Required
        lateinit var properties: FluidType.Properties
        var typeFactory: (FluidType.Properties) -> FluidType = ::FluidType
        var bucketFactory: ItemWithContextFactory<Fluid, Item> = ::BucketItem
        var fluidTag: String = name
        var bucketTag: String = "buckets/$name"
        // Optional

        fun build(): HTFluidContent {
            // Fluid Type
            val typeHolder: DeferredHolder<FluidType, FluidType> = typeRegister.register(name) { _ ->
                typeFactory(properties.descriptionId("block.${typeRegister.namespace}.$name"))
            }
            // Fluid Holder
            val sourceHolder: DeferredHolder<Fluid, FLUID> = DeferredHolder.create(Registries.FLUID, fluidRegister.createId(name))
            // Bucket Item
            val bucketHolder: HTSimpleItemHolderLike = itemRegister.registerItem(
                "${name}_bucket",
                { bucketFactory(sourceHolder.get(), it) },
                { it.stacksTo(1).craftRemainder(Items.BUCKET) },
            )
            val content: HTFluidContent = createContent(typeHolder.toLike(), sourceHolder.toLike(), bucketHolder)
            contentsCache[sourceHolder.key!!] = content
            return content
        }

        protected abstract fun createContent(
            typeHolder: HTHolderLike.HolderDelegate<FluidType, FluidType>,
            sourceHolder: HTHolderLike.HolderDelegate<Fluid, FLUID>,
            bucketHolder: HTSimpleItemHolderLike,
        ): HTFluidContent
    }

    inner class VirtualBuilder(name: String) : Builder<HTVirtualFluid>(name) {
        override fun createContent(
            typeHolder: HTHolderLike.HolderDelegate<FluidType, FluidType>,
            sourceHolder: HTHolderLike.HolderDelegate<Fluid, HTVirtualFluid>,
            bucketHolder: HTSimpleItemHolderLike,
        ): HTFluidContent {
            // Content
            fluidRegister.register(name, ::HTVirtualFluid.partially2(typeHolder, bucketHolder))
            return HTFluidContent(
                typeHolder,
                sourceHolder,
                bucketHolder,
                Registries.FLUID.createCommonTag(fluidTag),
                Registries.ITEM.createCommonTag(bucketTag),
                null,
                null,
            )
        }
    }

    inner class FlowingBuilder(name: String) : Builder<BaseFlowingFluid>(name) {
        var sourceFactory: (BaseFlowingFluid.Properties) -> BaseFlowingFluid.Source = BaseFlowingFluid::Source
        var flowingFactory: (BaseFlowingFluid.Properties) -> BaseFlowingFluid.Flowing = BaseFlowingFluid::Flowing

        var blockFactory: BlockWithContextFactory<BaseFlowingFluid, LiquidBlock>? = ::LiquidBlock
        var blockProperties: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = identity()

        override fun createContent(
            typeHolder: HTHolderLike.HolderDelegate<FluidType, FluidType>,
            sourceHolder: HTHolderLike.HolderDelegate<Fluid, BaseFlowingFluid>,
            bucketHolder: HTSimpleItemHolderLike,
        ): HTFluidContent {
            // Liquid Block
            val blockHolder: HTBlockHolderLike<LiquidBlock>?
            if (blockFactory == null) {
                blockHolder = null
            } else {
                blockHolder = blockRegister.registerBlock(
                    name,
                    BlockBehaviour.Properties
                        .of()
                        .let(blockProperties)
                        .noCollission()
                        .strength(100f)
                        .noLootTable()
                        .replaceable()
                        .pushReaction(PushReaction.DESTROY)
                        .liquid(),
                ) { prop: BlockBehaviour.Properties -> blockFactory!!(sourceHolder.get(), prop) }
            }
            // Fluid
            val flowingHolder: DeferredHolder<Fluid, BaseFlowingFluid.Flowing> =
                DeferredHolder.create(Registries.FLUID, fluidRegister.createId("flowing_$name"))
            val fluidProperties: BaseFlowingFluid.Properties = BaseFlowingFluid
                .Properties(typeHolder, sourceHolder, flowingHolder)
                .bucket(bucketHolder)
            blockHolder?.let(fluidProperties::block)
            fluidRegister.register(name) { _ -> sourceFactory(fluidProperties) }
            fluidRegister.register(flowingHolder.id.path) { _ -> flowingFactory(fluidProperties) }
            // Content
            return HTFluidContent(
                typeHolder,
                sourceHolder,
                bucketHolder,
                Registries.FLUID.createCommonTag(fluidTag),
                Registries.ITEM.createCommonTag(bucketTag),
                flowingHolder.toLike(),
                blockHolder,
            )
        }
    }
}
