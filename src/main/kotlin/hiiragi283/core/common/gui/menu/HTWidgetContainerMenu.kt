package hiiragi283.core.common.gui.menu

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.sync.HTSyncableSlot
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.common.gui.factory.HTWidgetHolderContext
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuType

/**
 * [HTWidgetHolder]に基づいた[HTContainerMenu]の拡張クラスです。
 */
class HTWidgetContainerMenu(
    menuType: MenuType<*>,
    containerId: Int,
    inventory: Inventory,
    context: HTWidgetHolderContext,
) : HTContainerMenu<HTWidgetHolderContext>(menuType, containerId, inventory, context) {
    val widgets: List<HTWidget> get() = _widgets
    private val _widgets: MutableList<HTWidget> = mutableListOf()
    private val widgetHolder: HTWidgetHolder = WidgetHolderImpl()

    init {
        context.setup(inventory.player, widgetHolder)
        // Player Inventory
        addPlayerInv(inventory)
    }

    override fun stillValid(player: Player): Boolean = context.stillValid(player)

    private inner class WidgetHolderImpl : HTWidgetHolder {
        override fun <WIDGET : HTWidget> addWidget(widget: WIDGET): WIDGET {
            _widgets += widget
            HiiragiCoreAPI.LOGGER.debug("Added widget: {}", widget)
            widget.setupHolder(this)
            if (widget is HTItemSlotWidget) {
                widget.containerSlot?.let(this@HTWidgetContainerMenu::addSlot)
            }
            return widget
        }

        override fun track(slot: HTSyncableSlot, type: HTSyncType) {
            this@HTWidgetContainerMenu.addTrackedSlot(slot, type)
            HiiragiCoreAPI.LOGGER.debug("Added syncable slot: {} for {}", slot, type)
        }
    }
}
