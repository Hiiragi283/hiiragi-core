package hiiragi283.core.impl.transfer.resolver

import hiiragi283.core.api.transfer.energy.SidedEnergyHandler
import hiiragi283.core.api.transfer.energy.StrictEnergyHandler
import hiiragi283.core.api.transfer.holder.HTEnergyHandlerHolder
import hiiragi283.core.impl.transfer.proxy.HTProxyEnergyHandler
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.energy.EnergyHandler

class HTEnergyHandlerManager(holder: HTEnergyHandlerHolder?, baseHandler: SidedEnergyHandler) :
    HTCapabilityManagerImpl<HTEnergyHandlerHolder, StrictEnergyHandler, EnergyHandler, SidedEnergyHandler>(
        holder,
        baseHandler,
        ::HTProxyEnergyHandler,
        { holder: HTEnergyHandlerHolder, side: Direction? -> listOfNotNull(holder.getEnergyHandler(side)) },
    )
