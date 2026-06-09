package hiiragi283.lib.registry

import hiiragi283.lib.HTConstants
import hiiragi283.lib.fluid.HTVirtualFluid
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.resource.toId
import hiiragi283.lib.tag.createTagKey
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
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
import net.neoforged.neoforge.fluids.BaseFlowingFluid.Flowing
import net.neoforged.neoforge.fluids.BaseFlowingFluid.Source
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.transfer.fluid.DispenseFluidContainer

class HTFluidContentRegister(modId: String) {
    private val fluidRegister: HTDeferredRegister<Fluid> = HTDeferredRegister(Registries.FLUID, modId)
    private val typeRegister = HTDeferredFluidTypeRegister(modId)
    private val blockRegister = HTDeferredBlockRegister(modId)
    private val itemRegister = HTDeferredItemRegister(modId)

    fun asFluidSequence(): Sequence<HTDeferredHolder<Fluid, *>> = fluidRegister.asSequence()

    fun asTypeSequence(): Sequence<HTDeferredFluidType<*>> = typeRegister.asSequence()

    fun asBlockSequence(): Sequence<HTDeferredBlock<*>> = blockRegister.asSequence()

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

    inline fun registerVirtual(name: String, builderAction: VirtualBuilder.() -> Unit): HTFluidContent.Virtual = VirtualBuilder(name).apply(builderAction).build()

    inline fun registerFlowing(name: String, builderAction: FlowingBuilder.() -> Unit): HTFluidContent.Flowing = FlowingBuilder(name).apply(builderAction).build()

    //    Builder    //

    @HTBuilderMarker
    abstract inner class Builder<FLUID : Fluid, CONTENT : HTFluidContent>(protected val name: String) {
        // Required
        lateinit var properties: FluidType.Properties
        var typeFactory: (FluidType.Properties) -> FluidType = ::FluidType
        var bucketFactory: ItemWithContextFactory<Fluid, Item> = ::BucketItem
        var fluidTag: Identifier = HTConstants.COMMON.toId(name)
        var bucketTag: Identifier = HTConstants.COMMON.toId("buckets/$name")
        // Optional

        fun build(): CONTENT {
            // Fluid Type
            val typeHolder: HTDeferredFluidType<FluidType> = typeRegister.registerType(name, properties.descriptionId("block.${typeRegister.namespace}.$name"), typeFactory)
            // Fluid Holder
            val sourceHolder: HTDeferredHolder<Fluid, FLUID> = HTDeferredHolder(Registries.FLUID, fluidRegister.createId(name))
            // Bucket Item
            val bucketHolder: HTSimpleDeferredItem = itemRegister.registerItem(
                "${name}_bucket",
                { bucketFactory(sourceHolder.get(), it) },
                { it.stacksTo(1).craftRemainder(Items.BUCKET) },
            )
            val content: CONTENT = createContent(typeHolder, sourceHolder, bucketHolder)
            contentsCache[sourceHolder.key] = content
            return content
        }

        protected abstract fun createContent(
            typeHolder: HTDeferredFluidType<FluidType>,
            sourceHolder: HTDeferredHolder<Fluid, FLUID>,
            bucketHolder: HTSimpleDeferredItem,
        ): CONTENT
    }

    inner class VirtualBuilder(name: String) : Builder<HTVirtualFluid, HTFluidContent.Virtual>(name) {
        override fun createContent(
            typeHolder: HTDeferredFluidType<FluidType>,
            sourceHolder: HTDeferredHolder<Fluid, HTVirtualFluid>,
            bucketHolder: HTSimpleDeferredItem,
        ): HTFluidContent.Virtual {
            // Content
            fluidRegister.register(name) { _ -> HTVirtualFluid(typeHolder, bucketHolder) }
            return HTFluidContent.Virtual(
                typeHolder,
                sourceHolder,
                bucketHolder,
                Registries.FLUID.createTagKey(fluidTag),
                Registries.ITEM.createTagKey(bucketTag),
            )
        }
    }

    inner class FlowingBuilder(name: String) : Builder<BaseFlowingFluid, HTFluidContent.Flowing>(name) {
        var sourceFactory: (BaseFlowingFluid.Properties) -> Source = BaseFlowingFluid::Source
        var flowingFactory: (BaseFlowingFluid.Properties) -> Flowing = BaseFlowingFluid::Flowing

        var blockFactory: ((BaseFlowingFluid, BlockBehaviour.Properties) -> LiquidBlock)? = ::LiquidBlock
        var blockProperties: Identity<BlockBehaviour.Properties> = identity()

        override fun createContent(
            typeHolder: HTDeferredFluidType<FluidType>,
            sourceHolder: HTDeferredHolder<Fluid, BaseFlowingFluid>,
            bucketHolder: HTSimpleDeferredItem,
        ): HTFluidContent.Flowing {
            // Liquid Block
            val blockHolder: SupplierWithId<LiquidBlock>?
            if (blockFactory == null) {
                blockHolder = null
            } else {
                blockHolder = blockRegister.registerBlock(
                    name,
                    BlockBehaviour.Properties
                        .of()
                        .let(blockProperties)
                        .noCollision()
                        .strength(100f)
                        .noLootTable()
                        .replaceable()
                        .pushReaction(PushReaction.DESTROY)
                        .liquid(),
                ) { prop: BlockBehaviour.Properties -> blockFactory!!(sourceHolder.get(), prop) }
            }
            // Fluid
            val flowingHolder: HTDeferredHolder<Fluid, Flowing> = HTDeferredHolder(Registries.FLUID, fluidRegister.createId("flowing_$name"))
            val fluidProperties: BaseFlowingFluid.Properties = BaseFlowingFluid
                .Properties(typeHolder, sourceHolder, flowingHolder)
                .bucket(bucketHolder)
            blockHolder?.let(fluidProperties::block)
            fluidRegister.register(name) { _ -> sourceFactory(fluidProperties) }
            fluidRegister.register(flowingHolder.id.path) { _ -> flowingFactory(fluidProperties) }
            // Content
            return HTFluidContent.Flowing(
                typeHolder,
                sourceHolder,
                bucketHolder,
                Registries.FLUID.createTagKey(fluidTag),
                Registries.ITEM.createTagKey(bucketTag),
                flowingHolder,
                blockHolder,
            )
        }
    }
}
