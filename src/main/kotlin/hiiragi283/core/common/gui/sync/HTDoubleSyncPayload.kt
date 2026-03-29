package hiiragi283.core.common.gui.sync

import hiiragi283.core.api.gui.sync.HTSyncableMenu
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.gui.sync.HTSyncableSlot
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

@JvmRecord
data class HTDoubleSyncPayload(val value: Double) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTDoubleSyncPayload> = ByteBufCodecs.DOUBLE
            .map(::HTDoubleSyncPayload, HTDoubleSyncPayload::value)
            .cast()

        @JvmField
        val TYPE: HTSyncablePayload.Type<HTDoubleSyncPayload> = HTSyncablePayload.Type.create(STREAM_CODEC)
    }

    override fun type(): HTSyncablePayload.Type<*> = TYPE

    override fun setValue(menu: HTSyncableMenu, index: Int) {
        val slot: HTSyncableSlot? = menu.getTrackedSlot(index)
        if (slot is HTDoubleSyncSlot) {
            slot.amountAsDouble = this.value
        }
    }
}
