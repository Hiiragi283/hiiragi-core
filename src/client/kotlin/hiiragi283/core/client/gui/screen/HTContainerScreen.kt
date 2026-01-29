package hiiragi283.core.client.gui.screen

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.sync.HTChangeType
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.gui.sync.HTSyncableSlot
import hiiragi283.core.common.gui.menu.HTContainerMenu
import hiiragi283.core.common.network.HTUpdateMenuPacket
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.core.RegistryAccess
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.network.PacketDistributor

@OnlyIn(Dist.CLIENT)
abstract class HTContainerScreen<MENU : HTContainerMenu<*>>(menu: MENU, inventory: Inventory, title: Component) :
    AbstractContainerScreen<MENU>(menu, inventory, title) {
    override fun render(
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        updateVisibility()
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    protected open fun updateVisibility() {}

    override fun containerTick() {
        super.containerTick()
        val player: Player = menu.inventory.player
        val access: RegistryAccess = player.registryAccess()
        HTUpdateMenuPacket
            .create(menu.containerId) {
                val trackedSlots: MutableList<Pair<HTSyncableSlot, HTSyncType>> = menu.trackedSlots
                for (i: Int in trackedSlots.indices) {
                    val (slot: HTSyncableSlot, syncType: HTSyncType) = trackedSlots[i]
                    if (!syncType.allowC2S) continue
                    val changeType: HTChangeType = slot.getChange() ?: continue
                    val payload: HTSyncablePayload = slot.createPayload(access, changeType) ?: continue
                    this[i] = payload
                    HiiragiCoreAPI.LOGGER.debug("Index: {}, Payload: {}", i, payload)
                }
            }?.let(PacketDistributor::sendToServer)
    }

    //    Extensions    //

    val startX: Int get() = (width - imageWidth) / 2

    val startY: Int get() = (height - imageHeight) / 2
}
