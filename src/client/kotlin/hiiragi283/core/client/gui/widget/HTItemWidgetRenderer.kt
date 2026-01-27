package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.util.HTSpriteRenderHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
abstract class HTItemWidgetRenderer<WIDGET : HTItemWidget>(widget: WIDGET) : HTAbstractWidgetRenderer<WIDGET>(widget) {
    override fun render(
        bounds: HTBounds,
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        HTSpriteRenderHelper.blit(guiGraphics, widget.backgroundType.slotTexture, bounds)
    }

    @OnlyIn(Dist.CLIENT)
    class SlotRenderer(widget: HTItemWidget.SlotWidget) : HTItemWidgetRenderer<HTItemWidget.SlotWidget>(widget)

    @OnlyIn(Dist.CLIENT)
    class StackRenderer(widget: HTItemWidget.StackWidget) : HTItemWidgetRenderer<HTItemWidget.StackWidget>(widget) {
        override fun render(
            bounds: HTBounds,
            guiGraphics: GuiGraphics,
            mouseX: Int,
            mouseY: Int,
            partialTick: Float,
        ) {
            // Render slot
            super.render(bounds, guiGraphics, mouseX, mouseY, partialTick)
            val font: Font = Minecraft.getInstance().font
            // Render stack
            val stack: ItemStack = widget.getItemStack()
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
}
