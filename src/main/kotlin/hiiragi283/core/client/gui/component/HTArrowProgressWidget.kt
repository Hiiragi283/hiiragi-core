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
class HTArrowProgressWidget(private val levelGetter: () -> Fraction, x: Int, y: Int) : HTAbstractWidget(x, y, 24, 16, Component.empty()) {
    private val texture: ResourceLocation = vanillaId("container", "furnace/burn_progress")

    override fun renderWidget(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        guiGraphics.blitSprite(
            texture,
            width,
            height,
            0,
            0,
            x,
            y,
            (levelGetter() * width).toInt(),
            height,
        )
    }
}
