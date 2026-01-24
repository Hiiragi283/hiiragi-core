package hiiragi283.core.api.mod

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import net.neoforged.neoforge.registries.NewRegistryEvent
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTCommonMod {
    init {
        NeoForgeMod.enableMilkFluid()

        val eventBus: IEventBus = MOD_BUS
        val container: ModContainer = LOADING_CONTEXT.activeContainer

        eventBus.addListener(::registerRegistries)
        eventBus.addListener(::commonSetup)
        eventBus.addListener { event: RegisterPayloadHandlersEvent ->
            container.modInfo.version
                .toString()
                .let(event::registrar)
                .let(::registerPayload)
        }

        initialize(eventBus, container)
    }

    protected abstract fun initialize(eventBus: IEventBus, container: ModContainer)

    protected open fun registerRegistries(event: NewRegistryEvent) {}

    protected open fun commonSetup(event: FMLCommonSetupEvent) {}

    protected open fun registerPayload(registrar: PayloadRegistrar) {}
}
