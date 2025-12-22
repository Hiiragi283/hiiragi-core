package hiiragi283.core.common.storage.resolver

import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.api.storage.energy.HTSidedEnergyStorage
import hiiragi283.core.api.storage.holder.HTEnergyBatteryHolder
import hiiragi283.core.common.storage.proxy.HTProxyEnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * @see mekanism.common.capabilities.resolver.manager.EnergyHandlerManager
 */
class HTEnergyStorageManager(holder: HTEnergyBatteryHolder?, baseHandler: HTSidedEnergyStorage) :
    HTCapabilityManagerImpl<HTEnergyBatteryHolder, HTEnergyBattery, IEnergyStorage, HTSidedEnergyStorage>(
        holder,
        baseHandler,
        ::HTProxyEnergyStorage,
        HTEnergyBatteryHolder::getEnergyBatteries,
    )
