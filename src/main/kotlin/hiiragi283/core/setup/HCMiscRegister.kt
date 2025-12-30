package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.common.inventory.slot.payload.HTFluidSyncPayload
import hiiragi283.core.common.inventory.slot.payload.HTFractionSyncPayload
import hiiragi283.core.common.inventory.slot.payload.HTIntSyncPayload
import hiiragi283.core.common.inventory.slot.payload.HTLongSyncPayload
import net.neoforged.neoforge.registries.RegisterEvent

object HCMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Slot Sync Type
        event.register(HiiragiCoreAPI.SLOT_TYPE_KEY) { helper ->
            helper.register(HTConst.COMMON.toId("fraction"), HTFractionSyncPayload.STREAM_CODEC)
            helper.register(HTConst.COMMON.toId("integer"), HTIntSyncPayload.STREAM_CODEC)
            helper.register(HTConst.COMMON.toId("long"), HTLongSyncPayload.STREAM_CODEC)

            helper.register(vanillaId("fluid"), HTFluidSyncPayload.STREAM_CODEC)
        }
    }
}
