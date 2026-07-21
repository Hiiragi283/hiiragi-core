package hiiragi283.core.client.gui.screen

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.HTGuiAccess
import hiiragi283.core.api.gui.sync.HTChangeType
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.gui.sync.HTSyncableSlot
import hiiragi283.core.api.text.Text
import hiiragi283.core.common.network.HTUpdateMenuPacket
import hiiragi283.core.support.gui.menu.HTContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.core.RegistryAccess
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.network.PacketDistributor

@OnlyIn(Dist.CLIENT)
abstract class HTContainerScreen<MENU : HTContainerMenu<*>>(menu: MENU, inventory: Inventory, title: Text) :
    AbstractContainerScreen<MENU>(menu, inventory, title),
    HTGuiAccess {
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
            .create(
                menu.containerId,
                menu.trackedSlots
                    .mapIndexedNotNull { index: Int, (slot: HTSyncableSlot, syncType: HTSyncType) ->
                        if (!syncType.allowC2S) return@mapIndexedNotNull null
                        val changeType: HTChangeType = slot.getChange() ?: return@mapIndexedNotNull null
                        val payload: HTSyncablePayload = slot.createPayload(access, changeType) ?: return@mapIndexedNotNull null
                        index to payload
                    }.onEach { (index: Int, payload: HTSyncablePayload) ->
                        HiiragiCoreAPI.LOGGER.debug("Index: {}, Payload: {}", index, payload)
                    }.toMap(),
            )?.let(PacketDistributor::sendToServer)
    }

    //    HTGuiAccess    //

    override val carried: ItemStack
        get() = menu.carried

    //    Extensions    //

    val startX: Int get() = (width - imageWidth) / 2

    val startY: Int get() = (height - imageHeight) / 2
}
