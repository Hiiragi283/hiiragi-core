package hiiragi283.core.api.gui.sync

/**
 * [HTSyncableSlot]を保持するGUIを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun interface HTSyncableMenu {
    fun getTrackedSlot(index: Int): HTSyncableSlot?
}
