package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTSlotWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.world.inventory.Slot

class HTItemSlotWidget(override val slot: Slot) :
    HTAbstractWidget(HTBounds.createSlot(slot.x, slot.y)),
    HTSlotWidget {
    override fun getType(): HTWidgetType<*> = HCWidgetTypes.ITEM_SLOT.get()
}
