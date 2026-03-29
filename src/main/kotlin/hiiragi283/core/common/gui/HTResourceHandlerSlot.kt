package hiiragi283.core.common.gui

import hiiragi283.core.api.gui.HTBackgroundType
import net.neoforged.neoforge.transfer.IndexModifier
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot

class HTResourceHandlerSlot(
    handler: ResourceHandler<ItemResource>,
    slotModifier: IndexModifier<ItemResource>,
    index: Int,
    xPosition: Int,
    yPosition: Int,
    val slotType: HTBackgroundType,
) : ResourceHandlerSlot(handler, slotModifier, index, xPosition, yPosition)
