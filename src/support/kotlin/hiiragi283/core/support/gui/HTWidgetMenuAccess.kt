package hiiragi283.core.support.gui

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.gui.widget.HTWidget
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.inventory.Slot

abstract class HTWidgetMenuAccess {
    companion object {
        @JvmField
        val INSTANCE: HTWidgetMenuAccess = HiiragiCoreAPI.getService()
    }

    abstract fun getContainerSlot(widget: HTWidget): Slot?

    abstract fun createMenuUpdatePacket(containerId: Int, builderAction: MutableMap<Int, HTSyncablePayload>.() -> Unit): CustomPacketPayload?
}
