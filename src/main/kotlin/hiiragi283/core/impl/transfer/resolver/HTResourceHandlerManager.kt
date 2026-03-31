package hiiragi283.core.impl.transfer.resolver

import hiiragi283.core.api.transfer.HTResourceSlot
import hiiragi283.core.api.transfer.SidedResourceHandler
import hiiragi283.core.api.transfer.holder.HTResourceSlotHolder
import hiiragi283.core.impl.transfer.proxy.HTProxyResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource

class HTResourceHandlerManager<T : Resource>(holder: HTResourceSlotHolder<T>?, baseHandler: SidedResourceHandler<T>) :
    HTCapabilityManagerImpl<HTResourceSlotHolder<T>, HTResourceSlot<T>, ResourceHandler<T>, SidedResourceHandler<T>>(
        holder,
        baseHandler,
        ::HTProxyResourceHandler,
        HTResourceSlotHolder<T>::getSlots,
    )
