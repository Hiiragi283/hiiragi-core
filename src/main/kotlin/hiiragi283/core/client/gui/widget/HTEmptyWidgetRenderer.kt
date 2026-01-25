package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetRenderer
import net.minecraft.client.gui.GuiGraphics

data object HTEmptyWidgetRenderer : HTWidgetRenderer<Nothing> {
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <WIDGET : HTWidget> create(widget: WIDGET): HTWidgetRenderer<WIDGET> = HTEmptyWidgetRenderer as HTWidgetRenderer<WIDGET>

    override fun render(
        bounds: HTBounds,
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {}
}
