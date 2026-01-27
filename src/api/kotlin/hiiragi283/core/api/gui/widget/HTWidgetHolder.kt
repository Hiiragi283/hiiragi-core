package hiiragi283.core.api.gui.widget

import hiiragi283.core.api.gui.sync.HTSyncableSlot

/**
 * [HTWidget]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see dev.emi.emi.api.widget.WidgetHolder
 */
interface HTWidgetHolder {
    /**
     * 指定した[widget]を追加します。
     * @param WIDGET [HTWidget]を実装したクラス
     */
    fun <WIDGET : HTWidget> addWidget(widget: WIDGET): WIDGET

    /**
     * 指定した[widget]を追加します。
     * @param WIDGET [HTWidget]を実装したクラス
     */
    operator fun <WIDGET : HTWidget> plusAssign(widget: WIDGET) {
        this.addWidget(widget)
    }

    /**
     * 指定した[slot]を追加します。
     * @see mekanism.common.inventory.container.MekanismContainer.track
     */
    fun track(slot: HTSyncableSlot)

    /**
     * 指定した[slot]を追加します。
     */
    operator fun plusAssign(slot: HTSyncableSlot) {
        this.track(slot)
    }
}
