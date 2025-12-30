package hiiragi283.core.api.inventory.container

import hiiragi283.core.api.inventory.slot.HTSyncableSlot

/**
 * [HTSyncableSlot]を保持するGUIを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun interface HTSyncableMenu {
    fun getTrackedSlot(index: Int): HTSyncableSlot?
}
