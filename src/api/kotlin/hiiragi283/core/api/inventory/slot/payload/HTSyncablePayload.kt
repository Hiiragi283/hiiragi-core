package hiiragi283.core.api.inventory.slot.payload

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.inventory.container.HTSyncableMenu
import hiiragi283.core.api.inventory.slot.HTSyncableSlot
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * [HTSyncableSlot]の同期に使用されるパケットを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see mekanism.common.network.to_client.container.property.PropertyData
 */
interface HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSyncablePayload> =
            ByteBufCodecs
                .registry(HiiragiCoreAPI.SLOT_TYPE_KEY)
                .dispatch(HTSyncablePayload::type, identity())
    }

    /**
     * パケットへの書き込みに使用される[StreamCodec]を返します。
     */
    fun type(): StreamCodec<RegistryFriendlyByteBuf, out HTSyncablePayload>

    /**
     * 指定された[menu]と[index]から値を更新します。
     */
    fun setValue(menu: HTSyncableMenu, index: Int)
}
