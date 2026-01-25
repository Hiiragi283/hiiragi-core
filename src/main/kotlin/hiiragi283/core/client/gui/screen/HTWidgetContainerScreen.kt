package hiiragi283.core.client.gui.screen

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetRenderer
import hiiragi283.core.client.gui.widget.HTWidgetRendererManager
import hiiragi283.core.common.gui.menu.HTWidgetContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTWidgetContainerScreen(menu: HTWidgetContainerMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTWidgetContainerMenu>(menu, inventory, title) {
    companion object {
        @JvmField
        val BACKGROUND: ResourceLocation = HiiragiCoreAPI.id("textures", "gui", "background.png")
    }

    override fun init() {
        super.init()
        menu.widgets.map(::WidgetWrapper).forEach(::addRenderableWidget)
    }

    /**
     * @see mekanism.client.gui.GuiMekanism.renderBg
     */
    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        guiGraphics.setColor(1f, 1f, 1f, 1f)
        guiGraphics.blit(BACKGROUND, startX, startY, 0, 0, imageWidth, imageHeight)
    }

    inner class WidgetWrapper<WIDGET : HTWidget>(val widget: WIDGET) :
        AbstractWidget(
            widget.bounds.x + startX,
            widget.bounds.y + startY,
            widget.bounds.width,
            widget.bounds.height,
            Component.empty(),
        ) {
        val bounds: HTBounds get() = HTBounds(this.x, this.y, this.width, this.height)

        private val renderer: HTWidgetRenderer<WIDGET>? by lazy { HTWidgetRendererManager.create(widget) }

        override fun renderWidget(
            guiGraphics: GuiGraphics,
            mouseX: Int,
            mouseY: Int,
            partialTick: Float,
        ) {
            renderer?.render(bounds, guiGraphics, mouseX, mouseY, partialTick)
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
