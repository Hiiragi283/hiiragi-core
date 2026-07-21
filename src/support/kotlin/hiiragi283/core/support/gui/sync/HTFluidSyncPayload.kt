package hiiragi283.core.support.gui.sync

import hiiragi283.core.api.gui.sync.HTSyncableMenu
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.gui.sync.HTSyncableSlot
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HTFluidSyncPayload(val value: FluidStack) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidSyncPayload> =
            FluidStack.OPTIONAL_STREAM_CODEC.map(::HTFluidSyncPayload, HTFluidSyncPayload::value)

        @JvmField
        val TYPE: HTSyncablePayload.Type<HTFluidSyncPayload> = HTSyncablePayload.Type(STREAM_CODEC)
    }

    override fun type(): HTSyncablePayload.Type<*> = TYPE

    @Suppress("UNCHECKED_CAST")
    override fun setValue(menu: HTSyncableMenu, index: Int) {
        val slot: HTSyncableSlot? = menu.getTrackedSlot(index)
        if (slot is HTFluidSyncSlot) {
            slot.asFluidStack = this.value
        }
    }
}
