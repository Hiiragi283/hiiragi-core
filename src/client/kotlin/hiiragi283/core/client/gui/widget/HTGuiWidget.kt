package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.gui.HTAbstractGui
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

/**
 * @see mekanism.client.gui.element.GuiElement
 */
class HTGuiWidget<WIDGET : HTWidget>(gui: HTAbstractGui, val widget: WIDGET) :
    AbstractWidget(
        widget.bounds.x + gui.getGuiLeft(),
        widget.bounds.y + gui.getGuiTop(),
        widget.bounds.width,
        widget.bounds.height,
        Component.empty(),
    ) {
    private val renderer: Renderable? by lazy { HTWidgetRendererManager.create(gui, widget) }

    init {
        if (widget is HTItemSlotWidget && widget.containerSlot != null) {
            active = false
        }
    }

    override fun renderWidget(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        if (visible) {
            renderer?.render(guiGraphics, mouseX, mouseY, partialTick)
        }
    }

    override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
        // widget.mouseClicked(null, mouseX, mouseY, button)
    }

    override fun onRelease(mouseX: Double, mouseY: Double) {
        widget.mouseReleased(mouseX, mouseY)
    }

    override fun onDrag(
        mouseX: Double,
        mouseY: Double,
        dragX: Double,
        dragY: Double,
    ) {
        widget.mouseDragged(mouseX, mouseY, dragX, dragY)
    }

    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        scrollX: Double,
        scrollY: Double,
    ): Boolean = widget.mouseScrolled(mouseX, mouseY, scrollX, scrollY)

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = widget.keyPressed(keyCode, scanCode, modifiers)

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean = widget.keyReleased(keyCode, scanCode, modifiers)

    override fun charTyped(codePoint: Char, modifiers: Int): Boolean = widget.charTyped(codePoint, modifiers)

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {}
}
