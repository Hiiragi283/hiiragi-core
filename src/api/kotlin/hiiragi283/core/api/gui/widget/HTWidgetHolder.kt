package hiiragi283.core.api.gui.widget

import hiiragi283.core.api.gui.sync.HTSyncableSlot

/**
 * [HTWidget]を管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see dev.emi.emi.api.widget.WidgetHolder
 */
interface HTWidgetHolder {
    val width: Int
    val height: Int

    fun <WIDGET : HTWidget> addWidget(widget: WIDGET): WIDGET

    operator fun <WIDGET : HTWidget> plusAssign(widget: WIDGET) {
        this.addWidget(widget)
    }

    /**
     * @see mekanism.common.inventory.container.MekanismContainer.track
     */
    fun track(slot: HTSyncableSlot)

    operator fun plusAssign(slot: HTSyncableSlot) {
        this.track(slot)
    }
}
