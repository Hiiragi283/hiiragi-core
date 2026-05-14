package hiiragi283.lib.gui.sync

import net.minecraft.core.RegistryAccess

/**
 * 任意の値をサーバーからクライアントへ同期可能なオブジェクトを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface HTSyncableSlot {
    /**
     * 現在の同期のフラグを取得します。
     * @return 同期を行わない場合は`null`
     */
    fun getChange(): HTChangeType?

    /**
     * 指定した[access]と[changeType]から[HTSyncablePayload]を作成します。
     * @return 同期を行わない場合は`null`
     */
    fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload?
}
