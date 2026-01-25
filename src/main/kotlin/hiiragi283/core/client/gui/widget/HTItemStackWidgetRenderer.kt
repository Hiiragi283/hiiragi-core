package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.gui.widget.HTItemStackWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTItemStackWidgetRenderer(widget: HTItemStackWidget) : HTAbstractWidgetRenderer<HTItemStackWidget>(widget) {
    override fun render(
        bounds: HTBounds,
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        val font: Font = Minecraft.getInstance().font
        // Render stack
        val stack: ItemStack = widget.getItemStack()
        if (stack.isEmpty) return
        val (x: Int, y: Int) = bounds
        guiGraphics.renderFakeItem(stack, x, y)
        guiGraphics.renderItemDecorations(font, stack, x, y)
        // Render tooltip
        if (bounds.contains(mouseX, mouseY)) {
            guiGraphics.renderTooltip(font, stack, mouseX, mouseY)
        }
    }
}
