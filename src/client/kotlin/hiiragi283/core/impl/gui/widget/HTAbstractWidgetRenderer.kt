package hiiragi283.core.impl.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.HTGuiAccess
import hiiragi283.core.api.gui.widget.HTWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * [HTWidget]向けの[Renderable]の抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@OnlyIn(Dist.CLIENT)
abstract class HTAbstractWidgetRenderer<WIDGET : HTWidget>(protected val gui: HTGuiAccess, protected val widget: WIDGET) : Renderable {
    final override fun render(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        val bounds: HTBounds = widget.bounds.offset(gui.getGuiLeft(), gui.getGuiTop())
        render(bounds, guiGraphics, mouseX, mouseY, partialTick)
    }

    protected abstract fun render(
        bounds: HTBounds,
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    )
}
