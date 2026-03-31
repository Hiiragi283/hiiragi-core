package hiiragi283.core.api.transfer.energy

import com.google.common.primitives.Ints
import net.neoforged.neoforge.transfer.energy.EnergyHandler

val EnergyHandler.neededAsLong: Long get() = maxOf(0, capacityAsLong - amountAsLong)

val EnergyHandler.neededAsInt: Int get() = Ints.saturatedCast(this.neededAsLong)
