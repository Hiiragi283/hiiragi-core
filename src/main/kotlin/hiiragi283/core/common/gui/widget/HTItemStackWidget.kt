package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTGhostWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.HTItemView
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.common.gui.sync.HTItemSyncSlot
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.world.item.ItemStack
import java.util.function.Consumer

class HTItemStackWidget(
    slot: HTItemSlot,
    stackSetter: Consumer<ItemStack>,
    x: Int,
    y: Int,
) : HTAbstractWidget(HTBounds.createSlot(x, y)),
    HTGhostWidget,
    HTItemView by slot {
    private val syncableSlot = HTItemSyncSlot(slot::getItemStack, stackSetter)

    override fun getType(): HTWidgetType<*> = HCWidgetTypes.ITEM_STACK.get()

    override fun setupHolder(widgetHolder: HTWidgetHolder) {
        widgetHolder.track(syncableSlot)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) {
        syncableSlot.asItemStack = ItemStack.EMPTY
    }

    //    HTGhostWidget    //

    private val consumer = HTGhostWidget.ItemConsumer { stack: Any ->
        if (stack is ItemStack) {
            syncableSlot.asItemStack = stack
        }
    }

    override fun getGhostConsumer(): HTGhostWidget.ItemConsumer = consumer
}
