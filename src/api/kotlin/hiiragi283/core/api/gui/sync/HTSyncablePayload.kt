package hiiragi283.core.api.gui.sync

import hiiragi283.core.api.HCRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * [HTSyncableSlot]の同期に使用されるパケットを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 * @see mekanism.common.network.to_client.container.property.PropertyData
 */
interface HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSyncablePayload> =
            ByteBufCodecs
                .registry(HCRegistries.Keys.SLOT_TYPE)
                .dispatch(HTSyncablePayload::type, Type<*>::streamCodec)
    }

    /**
     * パケットへの書き込みに使用される[StreamCodec]を返します。
     */
    fun type(): Type<*>

    /**
     * 指定された[menu]と[index]から値を更新します。
     */
    fun setValue(menu: HTSyncableMenu, index: Int)

    /**
     * [StreamCodec]のラッパークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    @ConsistentCopyVisibility
    @JvmRecord
    data class Type<PAYLOAD : HTSyncablePayload> private constructor(val streamCodec: StreamCodec<RegistryFriendlyByteBuf, PAYLOAD>) {
        companion object {
            @JvmStatic
            fun <PAYLOAD : HTSyncablePayload> create(streamCodec: StreamCodec<in RegistryFriendlyByteBuf, PAYLOAD>): Type<PAYLOAD> =
                Type(streamCodec.cast())
        }
    }
}
