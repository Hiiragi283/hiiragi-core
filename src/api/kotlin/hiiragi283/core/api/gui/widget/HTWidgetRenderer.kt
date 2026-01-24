package hiiragi283.core.api.gui.widget

import net.minecraft.client.gui.components.Renderable
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * [HTWidget]の描画を行うインターフェースです。
 * @param WIDGET [HTWidget]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@OnlyIn(Dist.CLIENT)
fun interface HTWidgetRenderer<WIDGET : HTWidget> : Renderable {
    /**
     * [HTWidget]から[HTWidgetRenderer]を作成するインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    fun interface Factory<WIDGET : HTWidget, RENDERER : HTWidgetRenderer<WIDGET>> {
        fun createRenderer(widget: WIDGET): RENDERER
    }
}
