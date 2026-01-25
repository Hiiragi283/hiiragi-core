package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTRegisterWidgetRendererEvent
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetRenderer
import hiiragi283.core.api.gui.widget.HTWidgetType
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.fml.ModLoader

/**
 * @see net.minecraft.client.gui.screens.MenuScreens
 */
@OnlyIn(Dist.CLIENT)
internal object HTWidgetRendererManager {
    @JvmStatic
    private lateinit var factories: Map<HTWidgetType<*>, HTWidgetRenderer.Factory<*, *>>

    @JvmStatic
    internal fun init() {
        val map: MutableMap<HTWidgetType<*>, HTWidgetRenderer.Factory<*, *>> = hashMapOf()
        HTRegisterWidgetRendererEvent(map::put).let(ModLoader::postEvent)
        this.factories = map
        HiiragiCoreAPI.LOGGER.info("Initialized Widget Renderer Manager")
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <WIDGET : HTWidget> getFactory(type: HTWidgetType<WIDGET>): HTWidgetRenderer.Factory<WIDGET, *>? =
        factories[type] as? HTWidgetRenderer.Factory<WIDGET, *>

    @Suppress("UNCHECKED_CAST")
    fun <WIDGET : HTWidget> create(widget: WIDGET): HTWidgetRenderer<WIDGET>? =
        getFactory(widget.getType() as HTWidgetType<WIDGET>)?.createRenderer(widget)
}
