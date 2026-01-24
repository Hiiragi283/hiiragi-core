package hiiragi283.core.api.gui.widget

import hiiragi283.core.api.gui.HTBounds

/**
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTWidget<WIDGET : HTWidget<WIDGET>> {
    fun getType(): HTWidgetType<WIDGET>

    fun getBound(): HTBounds
}
