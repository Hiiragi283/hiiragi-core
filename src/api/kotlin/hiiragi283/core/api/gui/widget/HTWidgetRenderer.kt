package hiiragi283.core.api.gui.widget

import net.minecraft.client.gui.GuiGraphics
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see net.minecraft.client.gui.components.Renderable
 */
@OnlyIn(Dist.CLIENT)
fun interface HTWidgetRenderer<WIDGET : HTWidget<WIDGET>> {
    fun render(
        widget: WIDGET,
        graphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    )

    /**
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    fun interface Factory<WIDGET : HTWidget<WIDGET>, RENDERER : HTWidgetRenderer<WIDGET>> {
        fun createRenderer(widget: WIDGET): RENDERER
    }
}
