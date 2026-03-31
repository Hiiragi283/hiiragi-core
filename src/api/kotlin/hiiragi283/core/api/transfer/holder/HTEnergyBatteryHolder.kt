package hiiragi283.core.api.transfer.holder

import hiiragi283.core.api.transfer.energy.HTEnergyBattery
import net.minecraft.core.Direction

interface HTEnergyBatteryHolder : HTCapabilityHolder {
    fun getBattery(side: Direction?): HTEnergyBattery?
}
