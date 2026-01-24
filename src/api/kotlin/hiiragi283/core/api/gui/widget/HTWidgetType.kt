package hiiragi283.core.api.gui.widget

import net.minecraft.resources.ResourceLocation

/**
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTWidgetType<WIDGET : HTWidget<WIDGET>> {
    /**
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    data class Simple<WIDGET : HTWidget<WIDGET>>(private val id: ResourceLocation) : HTWidgetType<WIDGET>
}
