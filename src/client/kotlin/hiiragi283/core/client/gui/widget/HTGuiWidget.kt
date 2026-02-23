package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.gui.HTAbstractGui
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.text.Text
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @see mekanism.client.gui.element.GuiElement
 */
@OnlyIn(Dist.CLIENT)
class HTGuiWidget<WIDGET : HTWidget>(private val gui: HTAbstractGui, val widget: WIDGET) :
    AbstractWidget(
        widget.bounds.x + gui.getGuiLeft(),
        widget.bounds.y + gui.getGuiTop(),
        widget.bounds.width,
        widget.bounds.height,
        Text.empty(),
    ) {
    private val access = Access()
    private val renderer: Renderable? by lazy { HTWidgetRendererManager.create(gui, widget) }

    init {
        widget.onInit(access)
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
        widget.mouseClicked(access, mouseX, mouseY, button)
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

    //    HTWidget.Access    //

    private inner class Access : HTWidget.Access {
        override var isActive: Boolean by this@HTGuiWidget::active
        override var isVisible: Boolean by this@HTGuiWidget::visible
        override val carried: ItemStack by this@HTGuiWidget.gui::carried
    }
}
