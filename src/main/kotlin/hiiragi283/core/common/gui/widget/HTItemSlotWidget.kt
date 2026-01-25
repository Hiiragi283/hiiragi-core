package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.gui.widget.HTWidgetType
import net.minecraft.world.inventory.Slot

class HTItemSlotWidget(x: Int, y: Int) : HTAbstractWidget(x, y, 18, 18) {
    private val slot: Slot? = null

    fun bindSlot(slot: Slot): HTItemSlotWidget = this

    override fun getType(): HTWidgetType<HTItemSlotWidget> {
        TODO("Not yet implemented")
    }

    override fun setupHolder(widgetHolder: HTWidgetHolder) {
        slot?.let(widgetHolder::addSlot)
    }
}
