package hiiragi283.core.api.transfer.energy

import hiiragi283.core.api.transfer.HTHandlerAccess
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

fun interface HTEnergyHandler : SidedEnergyHandler {
    fun hasEnergyHandler(): Boolean = true

    fun getBattery(side: Direction?): HTEnergyBattery?

    override fun getAmountAsLong(side: Direction?): Long = getBattery(side)?.amountAsLong ?: 0

    override fun getCapacityAsLong(side: Direction?): Long = getBattery(side)?.capacityAsLong ?: 0

    override fun insert(amount: Int, transaction: TransactionContext, side: Direction?): Int =
        getBattery(side)?.insert(amount, transaction, HTHandlerAccess.forHandler(side)) ?: 0

    override fun extract(amount: Int, transaction: TransactionContext, side: Direction?): Int =
        getBattery(side)?.extract(amount, transaction, HTHandlerAccess.forHandler(side)) ?: 0
}
