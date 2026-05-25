package hiiragi283.lib.transfer.holder

import hiiragi283.lib.transfer.energy.HTEnergyHandler
import net.minecraft.core.Direction

interface HTEnergyHandlerHolder : HTCapabilityHolder {
    fun getEnergyHandler(side: Direction?): HTEnergyHandler?
}
