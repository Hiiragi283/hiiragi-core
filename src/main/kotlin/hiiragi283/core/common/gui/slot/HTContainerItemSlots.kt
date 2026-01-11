package hiiragi283.core.common.gui.slot

import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import hiiragi283.core.common.storage.item.HTBasicItemSlot

fun HTContainerItemSlot.toSlot(): ItemSlot = ItemSlot().bind(this)

fun HTBasicItemSlot.toSlot(): ItemSlot = HTContainerItemSlot(this, 0, 0).toSlot()
