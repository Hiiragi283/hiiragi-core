package hiiragi283.core.client.gui.screen

import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetRenderer
import hiiragi283.core.client.gui.widget.HTWidgetRendererManager
import hiiragi283.core.common.gui.menu.HTWidgetContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTWidgetContainerScreen(menu: HTWidgetContainerMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTWidgetContainerMenu>(menu, inventory, title) {
    override fun init() {
        super.init()
        menu.widgets.map(::WidgetWrapper).forEach(::addRenderableWidget)
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
    }

    private class WidgetWrapper<WIDGET : HTWidget>(private val widget: WIDGET) :
        AbstractWidget(widget.getBound().x, widget.getBound().y, widget.getBound().width, widget.getBound().height, Component.empty()) {
        private val renderer: HTWidgetRenderer<WIDGET>? by lazy { HTWidgetRendererManager.create(widget) }

        override fun renderWidget(
            guiGraphics: GuiGraphics,
            mouseX: Int,
            mouseY: Int,
            partialTick: Float,
        ) {
            renderer?.render(guiGraphics, mouseX, mouseY, partialTick)
        }

        override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
            widget.mouseClicked(mouseX, mouseY, button)
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
}
