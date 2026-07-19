package hiiragi283.core.impl.gui.widget

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.event.HTRegisterWidgetRendererEvent
import hiiragi283.core.api.gui.HTAbstractGui
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetRendererFactory
import hiiragi283.core.api.gui.widget.HTWidgetType
import net.minecraft.client.gui.components.Renderable
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.fml.ModLoader
import net.minecraft.client.gui.screens.MenuScreens

/**
 * 参照 : [Minecraft - MenuScreens][MenuScreens]
 */
@OnlyIn(Dist.CLIENT)
internal object HTWidgetRendererManager {
    @JvmStatic
    private lateinit var factories: Map<HTWidgetType<*>, HTWidgetRendererFactory<*, *>>

    @JvmStatic
    fun init() {
        val map: MutableMap<HTWidgetType<*>, HTWidgetRendererFactory<*, *>> = hashMapOf()
        HTRegisterWidgetRendererEvent(map::put).let(ModLoader::postEvent)
        this.factories = map
        HiiragiCoreAPI.LOGGER.info("Initialized Widget Renderer Manager")
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <WIDGET : HTWidget> getFactory(type: HTWidgetType<WIDGET>): HTWidgetRendererFactory<WIDGET, *>? = factories[type] as? HTWidgetRendererFactory<WIDGET, *>

    @Suppress("UNCHECKED_CAST")
    fun <WIDGET : HTWidget> create(gui: HTAbstractGui, widget: WIDGET): Renderable? = getFactory(widget.getType() as HTWidgetType<WIDGET>)?.createRenderer(gui, widget)
}
