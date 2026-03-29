package hiiragi283.core.api.gui.widget

import hiiragi283.core.api.gui.HTAbstractGui
import hiiragi283.core.api.gui.HTBounds
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Renderable

/**
 * [HTWidget]向けの[Renderable]の抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTAbstractWidgetRenderer<WIDGET : HTWidget>(protected val gui: HTAbstractGui, protected val widget: WIDGET) : Renderable {
    final override fun extractRenderState(
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        a: Float,
    ) {
        val bounds: HTBounds = widget.bounds.offset(gui.getGuiLeft(), gui.getGuiTop())
        extractRenderState(bounds, graphics, mouseX, mouseY, a)
    }

    protected abstract fun extractRenderState(
        bounds: HTBounds,
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    )
}
