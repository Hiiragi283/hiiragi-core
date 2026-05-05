package hiiragi283.core.api.mod

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
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
     * パケットを登録します。
     */
    protected open fun registerPayload(registrar: PayloadRegistrar) {}

    /**
     * 追加のリソース/データパックを登録します。
     */
    protected open fun registerPack(event: AddPackFindersEvent) {}
}
