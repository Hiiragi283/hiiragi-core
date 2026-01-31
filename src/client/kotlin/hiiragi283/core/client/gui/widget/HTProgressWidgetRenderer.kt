package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.core.api.times
import hiiragi283.core.common.gui.widget.HTProgressWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTProgressWidgetRenderer(widget: HTProgressWidget) : HTAbstractWidgetRenderer<HTProgressWidget>(widget) {
    override fun render(
        bounds: HTBounds,
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        val texture: ResourceLocation = widget.texture ?: return
        val (x: Int, y: Int, width: Int, height: Int) = bounds
        guiGraphics.blitSprite(
            texture,
            width,
            height,
            0,
            0,
            x,
            y,
            (widget.getProgress() * width).toInt(),
            height,
        )
    }
}
