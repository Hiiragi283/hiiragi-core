package hiiragi283.core.impl.transfer.resolver

import hiiragi283.core.api.transfer.energy.HTEnergyBattery
import hiiragi283.core.api.transfer.energy.SidedEnergyHandler
import hiiragi283.core.api.transfer.holder.HTEnergyBatteryHolder
import hiiragi283.core.impl.transfer.proxy.HTProxyEnergyHandler
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.energy.EnergyHandler

class HTEnergyHandlerManager(holder: HTEnergyBatteryHolder?, baseHandler: SidedEnergyHandler) :
    HTCapabilityManagerImpl<HTEnergyBatteryHolder, HTEnergyBattery, EnergyHandler, SidedEnergyHandler>(
        holder,
        baseHandler,
        ::HTProxyEnergyHandler,
        { holder: HTEnergyBatteryHolder, side: Direction? -> listOfNotNull(holder.getBattery(side)) },
    )
