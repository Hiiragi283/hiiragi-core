package hiiragi283.core.api.integration.emi.widget

import dev.emi.emi.api.widget.Bounds
import dev.emi.emi.api.widget.Widget
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetRenderer
import hiiragi283.core.api.integration.emi.toEmi
import net.minecraft.client.gui.GuiGraphics

/**
 * @see mekanism.client.recipe_viewer.emi.widget.MekanismEmiWidget
 */
class HTEmiWidget(private val widget: HTWidget) : Widget() {
    private val bounds: HTBounds = widget.bounds
    private val emiBounds: Bounds = bounds.toEmi()
    private val renderer: HTWidgetRenderer<HTWidget>? by lazy { HiiragiCoreAccess.INSTANCE.createRenderer(widget) }

    override fun getBounds(): Bounds = emiBounds

    override fun render(
        draw: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        delta: Float,
    ) {
        renderer?.render(bounds, draw, mouseX, mouseY, delta)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int): Boolean {
        widget.mouseClicked(mouseX.toDouble(), mouseY.toDouble(), button)
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = widget.keyPressed(keyCode, scanCode, modifiers)
}
