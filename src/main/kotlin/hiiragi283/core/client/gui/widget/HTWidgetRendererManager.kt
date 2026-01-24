package hiiragi283.core.client.gui.widget

import com.mojang.logging.LogUtils
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
object HTWidgetRendererManager {
    @JvmStatic
    private val LOGGER = LogUtils.getLogger()

    @JvmStatic
    private lateinit var factories: Map<HTWidgetType<*>, HTWidgetRenderer.Factory<*, *>>

    @JvmStatic
    internal fun init() {
        val map: MutableMap<HTWidgetType<*>, HTWidgetRenderer.Factory<*, *>> = hashMapOf()
        HTRegisterWidgetRendererEvent(map::put).let(ModLoader::postEvent)
        this.factories = map
        LOGGER.info("Initialized Widget Renderer Manager")
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <WIDGET : HTWidget<WIDGET>> getFactory(type: HTWidgetType<WIDGET>): HTWidgetRenderer.Factory<WIDGET, *>? =
        factories[type] as? HTWidgetRenderer.Factory<WIDGET, *>

    fun <WIDGET : HTWidget<WIDGET>> create(widget: WIDGET): HTWidgetRenderer<WIDGET>? = getFactory(widget.getType())?.createRenderer(widget)
}
