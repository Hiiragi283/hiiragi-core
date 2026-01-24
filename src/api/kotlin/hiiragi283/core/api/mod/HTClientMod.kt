package hiiragi283.core.api.mod

import hiiragi283.core.api.event.HTRegisterWidgetRendererEvent
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTClientMod {
    init {
        val eventBus: IEventBus = MOD_BUS
        val container: ModContainer = LOADING_CONTEXT.activeContainer

        eventBus.addListener(::clientSetup)
        eventBus.addListener(::registerWidgetRenderer)
        eventBus.addListener(::registerBlockColors)
        eventBus.addListener(::registerItemColors)
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::registerScreens)
        eventBus.addListener(::registerEntityRenderer)

        initialize(eventBus, container)
    }

    protected abstract fun initialize(eventBus: IEventBus, container: ModContainer)

    protected fun configScreen(container: ModContainer) {
        container.registerExtensionPoint(IConfigScreenFactory::class.java, IConfigScreenFactory(::ConfigurationScreen))
    }

    protected open fun clientSetup(event: FMLClientSetupEvent) {}

    protected open fun registerWidgetRenderer(event: HTRegisterWidgetRendererEvent) {}

    protected open fun registerBlockColors(event: RegisterColorHandlersEvent.Block) {}

    protected open fun registerItemColors(event: RegisterColorHandlersEvent.Item) {}

    protected open fun registerClientExtensions(event: RegisterClientExtensionsEvent) {}

    protected open fun registerScreens(event: RegisterMenuScreensEvent) {}

    protected open fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {}
}
