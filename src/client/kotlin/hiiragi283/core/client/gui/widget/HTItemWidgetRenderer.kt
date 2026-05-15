package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.gui.HTAbstractGui
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.impl.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.core.util.HTSpriteRenderHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTItemWidgetRenderer(gui: HTAbstractGui, widget: HTItemWidget) : HTAbstractWidgetRenderer<HTItemWidget>(gui, widget) {
    override fun render(
        bounds: HTBounds,
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        // Render background
        HTSpriteRenderHelper.blit(guiGraphics, widget.backgroundType.slotTexture, bounds)
        // Render stack
        if (widget is HTItemWidget.Container) return
        val font: Font = Minecraft.getInstance().font
        val stack: ItemStack = widget.getStack()
        if (stack.isEmpty) return
        val (x: Int, y: Int) = bounds
        guiGraphics.renderFakeItem(stack, x + 1, y + 1)
        guiGraphics.renderItemDecorations(font, stack, x + 1, y + 1)
        // Render tooltip
        if (bounds.contains(mouseX, mouseY)) {
            guiGraphics.renderTooltip(font, stack, mouseX, mouseY)
        }
    }
}
