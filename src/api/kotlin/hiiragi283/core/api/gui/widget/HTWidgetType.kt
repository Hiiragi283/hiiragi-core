package hiiragi283.core.api.gui.widget

import net.minecraft.resources.ResourceLocation

/**
 * [HTWidget]を識別するためのインターフェースです。
 * @param WIDGET [HTWidget]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTWidgetType<WIDGET : HTWidget> {
    /**
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    data class Simple<WIDGET : HTWidget>(private val id: ResourceLocation) : HTWidgetType<WIDGET>
}
