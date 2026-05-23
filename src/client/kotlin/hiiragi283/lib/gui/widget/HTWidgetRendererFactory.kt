package hiiragi283.lib.gui.widget

import hiiragi283.lib.gui.HTAbstractGui
import net.minecraft.client.gui.components.Renderable

/**
 * [HTWidget]から[Renderable]を作成するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
fun interface HTWidgetRendererFactory<WIDGET : HTWidget, RENDERER : Renderable> {
    fun createRenderer(gui: HTAbstractGui, widget: WIDGET): RENDERER
}
