package hiiragi283.core.api.mod

import hiiragi283.core.api.capability.HTMultiCapability
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.api.storage.fluid.HTItemFluidHandler
import hiiragi283.core.api.storage.fluid.HTItemFluidTank
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ICapabilityProvider
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.event.AddPackFindersEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.DataPackRegistryEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * Hiiragi Coreとそれを前提とするmodで使用される，共通部分のmodの抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTCommonMod {
    init {
        NeoForgeMod.enableMilkFluid()

        val eventBus: IEventBus = MOD_BUS
        val container: ModContainer = LOADING_CONTEXT.activeContainer

        eventBus.addListener(::registerRegistries)
        eventBus.addListener(::registerDynamicRegistries)
        eventBus.addListener(::registerDataMapTypes)

        eventBus.addListener(::commonSetup)
        eventBus.addListener { event: RegisterPayloadHandlersEvent ->
            container.modInfo.version
                .toString()
                .let(event::registrar)
                .let(::registerPayload)
        }
        eventBus.addListener { event: RegisterCapabilitiesEvent -> registerCapabilities(CapabilityHelper(event)) }
        eventBus.addListener(::registerPack)

        initialize(eventBus, container)
    }

    /**
     * 初期化を行います。
     */
    protected abstract fun initialize(eventBus: IEventBus, container: ModContainer)

    /**
     * 新しいレジストリを登録します。
     */
    protected open fun registerRegistries(event: NewRegistryEvent) {}

    /**
     * 新しい動的レジストリを登録します。
     */
    protected open fun registerDynamicRegistries(event: DataPackRegistryEvent.NewRegistry) {}

    /**
     * 新しい[DataMapType]を登録します。
     */
    protected open fun registerDataMapTypes(event: RegisterDataMapTypesEvent) {}

    /**
     * レジストリへの登録後のセットアップを行います。
     */
    protected open fun commonSetup(event: FMLCommonSetupEvent) {}

    /**
     * キャパビリティの登録を行います。
     * @since 21.1.0
     */
    protected open fun registerCapabilities(helper: CapabilityHelper) {}

    @JvmInline
    protected value class CapabilityHelper(val event: RegisterCapabilitiesEvent) {
        //    Block    //

        fun <T : Any, BE : BlockEntity> registerBlockEntity(capability: HTMultiCapability<T, *>, type: BlockEntityType<BE>, provider: ICapabilityProvider<BE, Direction?, T>) {
            this.registerBlockEntity(capability.block, type, provider)
        }

        fun <T : Any, C, BE : BlockEntity> registerBlockEntity(capability: BlockCapability<T, C>, type: BlockEntityType<BE>, provider: ICapabilityProvider<BE, C, T>) {
            event.registerBlockEntity(capability, type, provider)
        }

        fun <BE> registerBlockEntity(type: BlockEntityType<BE>) where BE : BlockEntity, BE : HTHandlerProvider {
            this.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type) { blockEntity: BE, side: Direction? -> blockEntity.getItemHandler(side) }
            this.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type) { blockEntity: BE, side: Direction? -> blockEntity.getFluidHandler(side) }
        }

        //    Entity    //

        fun <T : Any, E : Entity> registerEntity(capability: HTMultiCapability<T, *>, type: EntityType<E>, provider: ICapabilityProvider<E, Direction?, T>) {
            this.registerEntity(capability.entity, type, provider)
        }

        fun <T : Any, C, E : Entity> registerEntity(capability: EntityCapability<T, C>, type: EntityType<E>, provider: ICapabilityProvider<E, C, T>) {
            event.registerEntity(capability, type, provider)
        }

        //    Item    //

        fun <T : Any> registerItem(capability: HTMultiCapability<*, T>, factory: (stack: ItemStack) -> T?, vararg items: ItemLike) {
            this.registerSimpleItem(capability.item, factory, *items)
        }

        fun <T : Any, C> registerItem(capability: ItemCapability<T, C>, provider: ICapabilityProvider<ItemStack, C, T>, vararg items: ItemLike) {
            event.registerItem(capability, provider, *items)
        }

        fun <T : Any> registerSimpleItem(capability: ItemCapability<T, in Nothing>, provider: (ItemStack) -> T?, vararg items: ItemLike) {
            this.registerItem(capability, { stack: ItemStack, _ -> provider(stack) }, *items)
        }

        fun registerItemTank(factory: (container: ItemStack) -> HTItemFluidTank?, vararg items: ItemLike) {
            this.registerSimpleItem(
                Capabilities.FluidHandler.ITEM,
                { stack: ItemStack -> HTItemFluidHandler { factory(stack) } },
                *items,
            )
        }
    }

    /**
     * パケットを登録します。
     */
    protected open fun registerPayload(registrar: PayloadRegistrar) {}

    /**
     * 追加のデータパックを登録します。
     */
    protected open fun registerPack(event: AddPackFindersEvent) {}
}
