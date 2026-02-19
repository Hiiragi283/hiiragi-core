package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.gui.HTAbstractGui
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.core.api.minus
import hiiragi283.core.api.times
import hiiragi283.core.common.gui.widget.HTFillDirection
import hiiragi283.core.common.gui.widget.HTProgressWidget
import hiiragi283.core.util.HTSpriteRenderHelper
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction

@OnlyIn(Dist.CLIENT)
class HTProgressWidgetRenderer(gui: HTAbstractGui, widget: HTProgressWidget) : HTAbstractWidgetRenderer<HTProgressWidget>(gui, widget) {
    override fun render(
        bounds: HTBounds,
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        val background: ResourceLocation = widget.backgroundTexture ?: return
        HTSpriteRenderHelper.blit(guiGraphics, background, bounds)

        val texture: ResourceLocation = widget.texture ?: return

        val progress: Fraction = widget.getProgress()
        val progressInverted: Fraction = 1 - progress
        val (x: Int, y: Int, width: Int, height: Int) = bounds

        val direction: HTFillDirection = widget.fillDirection
        val startX: Int = when (direction) {
            HTFillDirection.RIGHT_TO_LEFT -> x + (width * progressInverted).toInt()
            else -> x
        }
        val startY: Int = when (direction) {
            HTFillDirection.END_TO_TOP -> y + (height * progressInverted).toInt()
            else -> y
        }
        val widthFixed: Int = when (direction) {
            HTFillDirection.TOP_TO_END -> width
            HTFillDirection.END_TO_TOP -> width
            HTFillDirection.LEFT_TO_RIGHT -> width * progress
            HTFillDirection.RIGHT_TO_LEFT -> width * progress
        }.toInt()
        val heightFixed: Int = when (direction) {
            HTFillDirection.TOP_TO_END -> height * progress
            HTFillDirection.END_TO_TOP -> height * progress
            HTFillDirection.LEFT_TO_RIGHT -> height
            HTFillDirection.RIGHT_TO_LEFT -> height
        }.toInt()

        HTSpriteRenderHelper.blit(
            guiGraphics,
            texture,
            startX,
            startY,
            widthFixed,
            heightFixed,
            textureWidth = width,
            textureHeight = height,
        )
    }
}
