package hiiragi283.core.api.capability

import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

val IItemHandler.slotRange: IntRange get() = (0..<this.slots)

val IFluidHandler.tankRange: IntRange get() = (0..<this.tanks)
