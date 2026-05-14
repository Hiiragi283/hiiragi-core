package hiiragi283.lib.gui

import net.neoforged.neoforge.transfer.IndexModifier
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot

open class HTContainerItemSlot(
    handler: ResourceHandler<ItemResource>,
    slotModifier: IndexModifier<ItemResource>,
    handlerSlot: Int,
    xPosition: Int,
    yPosition: Int,
    val slotType: HTBackgroundType,
) : ResourceHandlerSlot(handler, slotModifier, handlerSlot, xPosition, yPosition) {
    constructor(handler: ItemStacksResourceHandler, handlerSlot: Int, xPosition: Int, yPosition: Int, slotType: HTBackgroundType) : this(handler, handler::set, handlerSlot, xPosition, yPosition, slotType)

    fun updateCount(count: Int) {
        stackCopy = stackCopy.copyWithCount(count)
    }
}
