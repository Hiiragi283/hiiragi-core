package hiiragi283.core.api.gui.widget

import net.minecraft.client.gui.components.Renderable
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@OnlyIn(Dist.CLIENT)
fun interface HTWidgetRenderer<WIDGET : HTWidget<WIDGET>> : Renderable {
    /**
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    fun interface Factory<WIDGET : HTWidget<WIDGET>, RENDERER : HTWidgetRenderer<WIDGET>> {
        fun createRenderer(widget: WIDGET): RENDERER
    }
}
