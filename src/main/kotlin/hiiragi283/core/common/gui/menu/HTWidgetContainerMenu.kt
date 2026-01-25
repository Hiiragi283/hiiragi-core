package hiiragi283.core.common.gui.menu

import hiiragi283.core.api.gui.sync.HTSyncableSlot
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.common.gui.factory.HTWidgetHolderContext
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot

/**
 * @see com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerMenu
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
    }

    override fun stillValid(player: Player): Boolean = context.stillValid(player)

    private inner class WidgetHolderImpl : HTWidgetHolder {
        override val width: Int
            get() = TODO("Not yet implemented")
        override val height: Int
            get() = TODO("Not yet implemented")

        override fun <WIDGET : HTWidget> addWidget(widget: WIDGET): WIDGET {
            _widgets += widget
            widget.setupHolder(this)
            return widget
        }

        override fun <SLOT : Slot> addSlot(slot: SLOT): SLOT {
            this@HTWidgetContainerMenu.addSlot(slot)
            return slot
        }

        override fun track(slot: HTSyncableSlot) {
            this@HTWidgetContainerMenu.trackedSlots += slot
        }
    }
}
