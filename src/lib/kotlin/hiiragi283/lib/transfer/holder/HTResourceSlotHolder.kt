package hiiragi283.lib.transfer.holder

import net.minecraft.core.Direction

interface HTResourceSlotHolder<SLOT> : HTCapabilityHolder {
    fun getSlots(side: Direction?): List<SLOT>
}
