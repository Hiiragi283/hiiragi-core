package hiiragi283.lib.gui.menu

import hiiragi283.lib.gui.factory.HTWidgetHolderContext
import hiiragi283.lib.gui.sync.HTSyncType
import hiiragi283.lib.gui.sync.HTSyncableSlot
import hiiragi283.lib.gui.widget.HTItemWidget
import hiiragi283.lib.gui.widget.HTWidget
import hiiragi283.lib.gui.widget.HTWidgetHolder
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuType

/**
 * [HTWidgetHolder]に基づいた[HTContainerMenu]の拡張クラスです。
 * @see net.minecraft.world.inventory.ChestMenu
 */
class HTWidgetContainerMenu(
    menuType: MenuType<*>,
    containerId: Int,
    inventory: Inventory,
    context: HTWidgetHolderContext,
) : HTContainerMenu<HTWidgetHolderContext>(menuType, containerId, inventory, context) {
    private val widgets: MutableList<HTWidget> = mutableListOf()
    val widgetHolder: HTWidgetHolder = WidgetHolderImpl()

    init {
        context.setup(inventory.player, widgetHolder)
        // Player Inventory
        addPlayerInv(inventory, (widgetHolder.rows - 4) * 18)
    }

    override fun stillValid(player: Player): Boolean = context.stillValid(player)

    private inner class WidgetHolderImpl : HTWidgetHolder {
        override fun <WIDGET : HTWidget> addWidget(widget: WIDGET): WIDGET {
            widgets += widget
            widget.setupHolder(this)
            if (widget is HTItemWidget.Container) {
                this@HTWidgetContainerMenu.addSlot(widget.slot)
            }
            return widget
        }

        override fun track(slot: HTSyncableSlot, type: HTSyncType) {
            this@HTWidgetContainerMenu.addTrackedSlot(slot, type)
        }

        override var rows: Int = 3

        override fun iterator(): Iterator<HTWidget> = widgets.iterator()
    }
}
