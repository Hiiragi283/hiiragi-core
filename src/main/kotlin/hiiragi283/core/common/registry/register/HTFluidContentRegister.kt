package hiiragi283.core.common.registry.register

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.fluid.HTVirtualFluid
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.BlockWithContextFactory
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.ItemWithContextFactory
import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.common.registry.HTDeferredFluid
import hiiragi283.core.common.registry.HTDeferredItem
import hiiragi283.core.common.registry.HTDeferredOnlyBlock
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
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTFluidContentRegister(modId: String) {
    private val fluidRegister = HTDeferredFluidRegister(modId)
    private val typeRegister: HTDeferredRegister<FluidType> = HTDeferredRegister(NeoForgeRegistries.Keys.FLUID_TYPES, modId)
    private val blockRegister = HTDeferredOnlyBlockRegister(modId)
    private val itemRegister = HTDeferredItemRegister(modId)

    fun asFluidSequence(): Sequence<HTDeferredFluid<*>> = fluidRegister.asSequence()

    fun asTypeSequence(): Sequence<HTDeferredHolder<FluidType, *>> = typeRegister.asSequence()

    fun asBlockSequence(): Sequence<HTDeferredOnlyBlock<*>> = blockRegister.asSequence()

    fun asItemSequence(): Sequence<HTDeferredItem<*>> = itemRegister.asSequence()

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
                for (item: HTDeferredItem<*> in asItemSequence()) {
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
            val typeHolder: HTDeferredHolder<FluidType, FluidType> = typeRegister.register(name) { _ ->
                typeFactory(properties.descriptionId("block.${typeRegister.namespace}.$name"))
            }
            // Fluid Holder
            val sourceHolder: HTDeferredFluid<FLUID> = HTDeferredFluid(fluidRegister.createId(name))
            // Bucket Item
            val bucketHolder: HTDeferredItem<Item> = itemRegister.registerItem(
                "${name}_bucket",
                { bucketFactory(sourceHolder.get(), it) },
                { it.stacksTo(1).craftRemainder(Items.BUCKET) },
            )
            val content: HTFluidContent = createContent(typeHolder, sourceHolder, bucketHolder)
            contentsCache[sourceHolder.key] = content
            return content
        }

        protected abstract fun createContent(
            typeHolder: HTDeferredHolder<FluidType, FluidType>,
            sourceHolder: HTDeferredFluid<FLUID>,
            bucketHolder: HTDeferredItem<Item>,
        ): HTFluidContent
    }

    inner class VirtualBuilder(name: String) : Builder<HTVirtualFluid>(name) {
        override fun createContent(
            typeHolder: HTDeferredHolder<FluidType, FluidType>,
            sourceHolder: HTDeferredFluid<HTVirtualFluid>,
            bucketHolder: HTDeferredItem<Item>,
        ): HTFluidContent {
            // Content
            val content = HTFluidContent(
                typeHolder,
                sourceHolder,
                bucketHolder,
                Registries.FLUID.createCommonTag(fluidTag),
                Registries.ITEM.createCommonTag(bucketTag),
                null,
                null,
            )
            fluidRegister.register(name, ::HTVirtualFluid.partially1(content))
            return content
        }
    }

    inner class FlowingBuilder(name: String) : Builder<BaseFlowingFluid>(name) {
        var sourceFactory: (BaseFlowingFluid.Properties) -> BaseFlowingFluid.Source = BaseFlowingFluid::Source
        var flowingFactory: (BaseFlowingFluid.Properties) -> BaseFlowingFluid.Flowing = BaseFlowingFluid::Flowing

        var blockFactory: BlockWithContextFactory<BaseFlowingFluid, LiquidBlock>? = ::LiquidBlock
        var blockProperties: (BlockBehaviour.Properties) -> BlockBehaviour.Properties = identity()

        override fun createContent(
            typeHolder: HTDeferredHolder<FluidType, FluidType>,
            sourceHolder: HTDeferredFluid<BaseFlowingFluid>,
            bucketHolder: HTDeferredItem<Item>,
        ): HTFluidContent {
            // Liquid Block
            val blockHolder: HTDeferredOnlyBlock<LiquidBlock>?
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
            val flowingHolder: HTDeferredHolder<Fluid, BaseFlowingFluid.Flowing> =
                HTDeferredHolder(Registries.FLUID, fluidRegister.createId("flowing_$name"))
            val fluidProperties: BaseFlowingFluid.Properties = BaseFlowingFluid
                .Properties(typeHolder, sourceHolder, flowingHolder)
                .bucket(bucketHolder)
            blockHolder?.let(fluidProperties::block)
            fluidRegister.register(name) { _ -> sourceFactory(fluidProperties) }
            fluidRegister.register(flowingHolder.path) { _ -> flowingFactory(fluidProperties) }
            // Content
            return HTFluidContent(
                typeHolder,
                sourceHolder,
                bucketHolder,
                Registries.FLUID.createCommonTag(fluidTag),
                Registries.ITEM.createCommonTag(bucketTag),
                flowingHolder,
                blockHolder,
            )
        }
    }
}
