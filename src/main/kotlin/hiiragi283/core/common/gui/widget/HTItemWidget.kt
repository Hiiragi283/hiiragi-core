package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.integration.emi.widget.HTGhostWidget
import hiiragi283.core.api.integration.emi.widget.HTIngredientWidget
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.HTItemView
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.gui.sync.HTItemSyncSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import java.util.function.Consumer

abstract class HTItemWidget(bounds: HTBounds) :
    HTAbstractWidget(bounds),
    HTIngredientWidget {
    open var backgroundType: HTBackgroundType = HTBackgroundType.NONE

    fun setBackground(background: HTBackgroundType): HTItemWidget = apply {
        this.backgroundType = background
    }

    class SlotWidget(val slot: Slot) : HTItemWidget(HTBounds.createSlot(slot.x - 1, slot.y - 1)) {
        override fun getType(): HTWidgetType<*> = HCWidgetTypes.ITEM_SLOT.get()

        override fun getIngredient(): ItemStack = slot.item
    }

    class StackWidget(
        slot: HTItemSlot,
        stackSetter: Consumer<ItemStack>?,
        x: Int,
        y: Int,
    ) : HTItemWidget(HTBounds.createSlot(x - 1, y - 1)),
        HTGhostWidget,
        HTItemView by slot {
        constructor(slot: HTItemSlot, x: Int, y: Int) : this(slot, null, x, y)

        private val syncableSlot: HTItemSyncSlot? = stackSetter?.let { HTItemSyncSlot(slot::getItemStack, it) }

        override fun getType(): HTWidgetType<*> = HCWidgetTypes.ITEM_STACK.get()

        override fun setupHolder(widgetHolder: HTWidgetHolder) {
            val slot: HTItemSyncSlot = syncableSlot ?: return
            widgetHolder.track(slot, HTSyncType.BOTH)
        }

        override fun mouseClicked(
            menu: AbstractContainerMenu,
            mouseX: Double,
            mouseY: Double,
            button: Int,
        ) {
            syncableSlot?.asItemStack = menu.carried.copy()
        }

        override fun getIngredient(): ItemStack = this.getItemStack()

        //    HTGhostWidget    //

        private var consumer: HTGhostWidget.ItemConsumer = HTGhostWidget.ItemConsumer { stack: Any ->
            if (stack is ItemStack) {
                syncableSlot?.asItemStack = stack
            }
        }

        override fun getGhostConsumer(): HTGhostWidget.ItemConsumer = consumer
    }
}
