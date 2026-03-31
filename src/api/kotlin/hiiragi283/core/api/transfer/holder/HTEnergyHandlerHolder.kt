package hiiragi283.core.api.transfer.holder

import hiiragi283.core.api.transfer.energy.StrictEnergyHandler
import net.minecraft.core.Direction

interface HTEnergyHandlerHolder : HTCapabilityHolder {
    fun getEnergyHandler(side: Direction?): StrictEnergyHandler?
}
