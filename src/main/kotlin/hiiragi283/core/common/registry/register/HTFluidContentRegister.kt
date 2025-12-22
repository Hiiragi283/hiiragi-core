package hiiragi283.core.common.registry.register

import hiiragi283.core.api.registry.BlockWithContextFactory
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.api.registry.ItemWithContextFactory
import hiiragi283.core.api.tag.createCommonTag
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
import java.util.function.UnaryOperator

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

    fun registerSimple(
        name: String,
        properties: FluidType.Properties,
        blockFactory: BlockWithContextFactory<BaseFlowingFluid.Source, LiquidBlock> = ::LiquidBlock,
        bucketFactory: ItemWithContextFactory<BaseFlowingFluid.Source, Item> = ::BucketItem,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTSimpleFluidContent = register(name, properties, ::FluidType, blockFactory, bucketFactory, blockProperties)

    fun <TYPE : FluidType> register(
        name: String,
        properties: FluidType.Properties,
        typeFactory: (FluidType.Properties) -> TYPE,
        blockFactory: BlockWithContextFactory<BaseFlowingFluid.Source, LiquidBlock> = ::LiquidBlock,
        bucketFactory: ItemWithContextFactory<BaseFlowingFluid.Source, Item> = ::BucketItem,
        blockProperties: UnaryOperator<BlockBehaviour.Properties> = UnaryOperator.identity(),
    ): HTFluidContent<TYPE, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> {
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
        val bucket: HTDeferredItem<Item> = itemRegister.registerItem(
            "${name}_bucket",
            { bucketFactory(stillHolder.get(), it) },
            { it.stacksTo(1).craftRemainder(Items.BUCKET) },
        )
        // Liquid Block
        val liquidBlock: HTDeferredOnlyBlock<LiquidBlock> = blockRegister.registerBlock(
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
            .block(liquidBlock)
            .bucket(bucket)
        val stillPath: String = stillHolder.getPath()
        fluidRegister.register(stillPath) { _ -> BaseFlowingFluid.Source(fluidProperties) }
        fluidRegister.register(flowingHolder.getPath()) { _ -> BaseFlowingFluid.Flowing(fluidProperties) }
        // Contents
        val contents: HTFluidContent<TYPE, BaseFlowingFluid.Source, BaseFlowingFluid.Flowing> = HTFluidContent(
            typeHolder,
            stillHolder,
            flowingHolder,
            Registries.FLUID.createCommonTag(stillPath),
            liquidBlock,
            bucket,
            Registries.ITEM.createCommonTag("buckets", stillPath),
        )
        contentsCache[stillHolder.key] = contents
        return contents
    }
}
