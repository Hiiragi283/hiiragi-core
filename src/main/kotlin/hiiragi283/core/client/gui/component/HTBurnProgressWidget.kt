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
class HTBurnProgressWidget(private val levelGetter: () -> Fraction, x: Int, y: Int) :
    HTAbstractWidget(x, y, 14, 14, Component.empty()) {
    private val texture: ResourceLocation = vanillaId("container", "furnace/lit_progress")

    override fun renderWidget(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        val v: Int = (levelGetter() * height).toInt()
        if (v <= 0) return
        guiGraphics.blitSprite(
            texture,
            width,
            height,
            0,
            v,
            x,
            y + v,
            width,
            height - v,
        )
    }
}
