package hiiragi283.core.client.gui.component

import hiiragi283.core.api.math.times
import hiiragi283.core.api.resource.vanillaId
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.apache.commons.lang3.math.Fraction

@OnlyIn(Dist.CLIENT)
class HTProgressWidget(
    private val texture: ResourceLocation,
    private val levelGetter: () -> Fraction,
    private val isHorizontal: Boolean,
    x: Int,
    y: Int,
    width: Int,
    height: Int,
) : HTAbstractWidget(x, y, width, height, Component.empty()) {
    companion object {
        @JvmStatic
        fun arrow(levelGetter: () -> Fraction, x: Int, y: Int): HTProgressWidget =
            HTProgressWidget(vanillaId("container", "furnace/burn_progress"), levelGetter, true, x, y, 24, 16)

        @JvmStatic
        fun burn(levelGetter: () -> Fraction, x: Int, y: Int): HTProgressWidget =
            HTProgressWidget(vanillaId("container", "furnace/lit_progress"), levelGetter, false, x, y, 14, 14)
    }

    override fun renderWidget(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        val width: Int = when (isHorizontal) {
            true -> (levelGetter() * width).toInt()
            false -> width
        }
        val height: Int = when (isHorizontal) {
            true -> height
            false -> (levelGetter() * height).toInt()
        }
        guiGraphics.blitSprite(
            texture,
            this.width,
            this.height,
            0,
            0,
            x,
            y,
            width,
            height,
        )
    }
}
