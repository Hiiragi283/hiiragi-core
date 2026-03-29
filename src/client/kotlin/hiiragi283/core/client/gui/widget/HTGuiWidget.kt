package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.gui.HTAbstractGui
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.text.Text
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.world.item.ItemStack

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

    override fun extractWidgetRenderState(
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        a: Float,
    ) {
        if (visible) {
            renderer?.extractRenderState(graphics, mouseX, mouseY, a)
        }
    }

    override fun updateWidgetNarration(output: NarrationElementOutput) {}

    //    HTWidget.Access    //

    private inner class Access : HTWidget.Access {
        override var isActive: Boolean by this@HTGuiWidget::active
        override var isVisible: Boolean by this@HTGuiWidget::visible
        override val carried: ItemStack by this@HTGuiWidget.gui::carried
    }
}
