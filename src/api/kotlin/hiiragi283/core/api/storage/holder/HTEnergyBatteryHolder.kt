package hiiragi283.core.api.storage.holder

import hiiragi283.core.api.storage.energy.HTEnergyBattery
import net.minecraft.core.Direction

/**
 * [HTEnergyBattery]向けの[HTCapabilityHolder]の拡張インターフェース
 * @see mekanism.common.capabilities.holder.energy.IEnergyContainerHolder
 */
interface HTEnergyBatteryHolder : HTCapabilityHolder {
    fun getEnergyBatteries(side: Direction?): List<HTEnergyBattery>
}
