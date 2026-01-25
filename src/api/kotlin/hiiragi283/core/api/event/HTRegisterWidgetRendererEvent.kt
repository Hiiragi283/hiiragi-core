package hiiragi283.core.api.event

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetRenderer
import hiiragi283.core.api.gui.widget.HTWidgetType
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent

/**
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
 */
@OnlyIn(Dist.CLIENT)
class HTRegisterWidgetRendererEvent(
    private val registerer: (HTWidgetType<*>, HTWidgetRenderer.Factory<*, *>) -> HTWidgetRenderer.Factory<*, *>?,
) : Event(),
    IModBusEvent {
    fun <WIDGET : HTWidget, RENDERER : HTWidgetRenderer<WIDGET>> register(
        type: HTWidgetType<WIDGET>,
        factory: HTWidgetRenderer.Factory<WIDGET, RENDERER>,
    ) {
        check(registerer(type, factory) == null) { "Duplicated widget renderer for ${HCRegistries.WIDGET_TYPE.getKey(type)}" }
    }
}
