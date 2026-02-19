package hiiragi283.core.client

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.gui.HTAbstractGui
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.client.gui.widget.HTWidgetRendererManager
import net.minecraft.client.gui.components.Renderable

class HiiragiCoreAccessClientImpl : HiiragiCoreAccess.Client {
    override fun <WIDGET : HTWidget> createRenderer(gui: HTAbstractGui, widget: WIDGET): Renderable? =
        HTWidgetRendererManager.create(gui, widget)
}
