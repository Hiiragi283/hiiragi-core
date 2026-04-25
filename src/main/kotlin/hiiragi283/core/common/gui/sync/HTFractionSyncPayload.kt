package hiiragi283.core.common.gui.sync

import hiiragi283.core.api.gui.sync.HTSyncableMenu
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.gui.sync.HTSyncableSlot
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import org.apache.commons.lang3.math.Fraction

@JvmRecord
data class HTFractionSyncPayload(val value: Fraction) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFractionSyncPayload> = HTStreamCodecs.FRACTION
            .map(::HTFractionSyncPayload, HTFractionSyncPayload::value)
            .cast()

        @JvmField
        val TYPE: HTSyncablePayload.Type<HTFractionSyncPayload> = HTSyncablePayload.Type(STREAM_CODEC)
    }

    override fun type(): HTSyncablePayload.Type<*> = TYPE

    override fun setValue(menu: HTSyncableMenu, index: Int) {
        val slot: HTSyncableSlot? = menu.getTrackedSlot(index)
        if (slot is HTFractionSyncSlot) {
            slot.amountAsFraction = this.value
        }
    }
}
