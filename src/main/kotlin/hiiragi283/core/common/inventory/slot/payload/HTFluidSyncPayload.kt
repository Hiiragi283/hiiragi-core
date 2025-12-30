package hiiragi283.core.common.inventory.slot.payload

import hiiragi283.core.api.inventory.container.HTSyncableMenu
import hiiragi283.core.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.common.inventory.slot.HTFluidSyncSlot
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @see mekanism.common.network.to_client.container.property.FluidStackPropertyData
 */
@JvmRecord
data class HTFluidSyncPayload(val stack: FluidStack) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidSyncPayload> =
            VanillaBiCodecs.FLUID_STACK.streamCodec
                .map(::HTFluidSyncPayload, HTFluidSyncPayload::stack)
    }

    override fun type(): StreamCodec<RegistryFriendlyByteBuf, HTFluidSyncPayload> = STREAM_CODEC

    @Suppress("UNCHECKED_CAST")
    override fun setValue(menu: HTSyncableMenu, index: Int) {
        (menu.getTrackedSlot(index) as? HTFluidSyncSlot)?.setStack(this.stack)
    }
}
