package hiiragi283.core.api.gui.widget

import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.sync.HTSyncableSlot

/**
 * [HTWidget]を保持するインターフェースです。
 *
 * 参照 : [EMI - WidgetHolder](https://github.com/emilyploszaj/emi/blob/1.21/xplat/src/main/java/dev/emi/emi/api/widget/WidgetHolder.java)
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTWidgetHolder : Iterable<HTWidget> {
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
     * @param type 同期の方向
     * @see mekanism.common.inventory.container.MekanismContainer.track
     */
    fun track(slot: HTSyncableSlot, type: HTSyncType)

    operator fun plusAssign(pair: Pair<HTSyncableSlot, HTSyncType>) {
        val (slot: HTSyncableSlot, type: HTSyncType) = pair
        this.track(slot, type)
    }

    var rows: Int
}
