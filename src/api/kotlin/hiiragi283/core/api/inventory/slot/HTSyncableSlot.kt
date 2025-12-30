package hiiragi283.core.api.inventory.slot

import hiiragi283.core.api.inventory.slot.payload.HTSyncablePayload
import net.minecraft.core.RegistryAccess

/**
 * 任意の値を同期可能なスロットを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see mekanism.common.inventory.container.sync.ISyncableData
 */
interface HTSyncableSlot {
    /**
     * 現在の同期のフラグを取得します。
     */
    fun getChange(): HTChangeType

    /**
     * 指定した[access]と[changeType]から同期用のパケットを作成します。
     * @return 同期を行わない場合は`null`
     */
    fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload?
}
