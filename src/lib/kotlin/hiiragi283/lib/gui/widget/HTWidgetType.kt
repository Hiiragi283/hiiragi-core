package hiiragi283.lib.gui.widget

import net.minecraft.resources.Identifier

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
    data class Simple<WIDGET : HTWidget>(private val id: Identifier) : HTWidgetType<WIDGET>
}
