package hiiragi283.core.common.storage.resolver

import hiiragi283.core.api.storage.holder.HTItemSlotHolder
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.HTSidedItemHandler
import hiiragi283.core.common.storage.proxy.HTProxyItemHandler
import net.neoforged.neoforge.items.IItemHandler

/**
 * @see mekanism.common.capabilities.resolver.manager.ItemHandlerManager
 */
class HTItemHandlerManager(holder: HTItemSlotHolder?, baseHandler: HTSidedItemHandler) :
    HTCapabilityManagerImpl<HTItemSlotHolder, HTItemSlot, IItemHandler, HTSidedItemHandler>(
        holder,
        baseHandler,
        ::HTProxyItemHandler,
        HTItemSlotHolder::getItemSlot,
    )
