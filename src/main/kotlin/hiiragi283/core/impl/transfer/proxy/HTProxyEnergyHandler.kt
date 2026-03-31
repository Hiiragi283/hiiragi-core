package hiiragi283.core.impl.transfer.proxy

import hiiragi283.core.api.transfer.energy.SidedEnergyHandler
import hiiragi283.core.api.transfer.holder.HTCapabilityHolder
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTProxyEnergyHandler(private val handler: SidedEnergyHandler, side: Direction?, holder: HTCapabilityHolder?) :
    HTProxyHandler(side, holder),
    EnergyHandler {
    override fun getAmountAsLong(): Long = handler.getAmountAsLong(side)

    override fun getCapacityAsLong(): Long = handler.getCapacityAsLong(side)

    override fun insert(amount: Int, transaction: TransactionContext): Int = when {
        readOnlyInsert -> 0
        else -> handler.insert(amount, transaction, side)
    }

    override fun extract(amount: Int, transaction: TransactionContext): Int = when {
        readOnlyExtract -> 0
        else -> handler.extract(amount, transaction, side)
    }
}
