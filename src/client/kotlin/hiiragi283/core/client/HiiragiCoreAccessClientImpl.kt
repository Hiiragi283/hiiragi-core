package hiiragi283.core.client

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetRenderer
import hiiragi283.core.client.gui.widget.HTWidgetRendererManager

class HiiragiCoreAccessClientImpl : HiiragiCoreAccess.Client {
    override fun <WIDGET : HTWidget> createRenderer(widget: WIDGET): HTWidgetRenderer<WIDGET>? = HTWidgetRendererManager.create(widget)
}
