package hiiragi283.core.api.gui.widget

import hiiragi283.core.api.gui.HTAbstractGui
import net.minecraft.client.gui.components.Renderable
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * [HTWidget]から[Renderable]を作成するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
@OnlyIn(Dist.CLIENT)
fun interface HTWidgetRendererFactory<WIDGET : HTWidget, RENDERER : Renderable> {
    fun createRenderer(gui: HTAbstractGui, widget: WIDGET): RENDERER
}
