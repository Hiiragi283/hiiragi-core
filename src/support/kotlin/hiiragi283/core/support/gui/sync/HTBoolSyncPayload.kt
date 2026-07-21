package hiiragi283.core.support.gui.sync

import hiiragi283.core.api.gui.sync.HTSyncableMenu
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.gui.sync.HTSyncableSlot
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

@JvmRecord
data class HTBoolSyncPayload(val value: Boolean) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTBoolSyncPayload> = ByteBufCodecs.BOOL
            .map(::HTBoolSyncPayload, HTBoolSyncPayload::value)
            .cast()

        @JvmField
        val TYPE: HTSyncablePayload.Type<HTBoolSyncPayload> = HTSyncablePayload.Type(STREAM_CODEC)
    }

    override fun type(): HTSyncablePayload.Type<*> = TYPE

    override fun setValue(menu: HTSyncableMenu, index: Int) {
        val slot: HTSyncableSlot? = menu.getTrackedSlot(index)
        if (slot is HTBoolSyncSlot) {
            slot.asBool = this.value
        }
    }
}
