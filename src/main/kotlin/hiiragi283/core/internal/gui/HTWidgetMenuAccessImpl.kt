package hiiragi283.core.internal.gui

import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.common.network.HTUpdateMenuPacket
import hiiragi283.core.support.gui.HTWidgetMenuAccess
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.inventory.Slot

class HTWidgetMenuAccessImpl : HTWidgetMenuAccess() {
    override fun getContainerSlot(widget: HTWidget): Slot? = when (widget) {
        is HTItemWidget.Container -> widget.slot
        else -> null
    }

    override fun createMenuUpdatePacket(containerId: Int, builderAction: MutableMap<Int, HTSyncablePayload>.() -> Unit): CustomPacketPayload? = HTUpdateMenuPacket.create(containerId, builderAction)
}
