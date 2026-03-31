package hiiragi283.core.api.transfer.energy

import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface SidedEnergyHandler : EnergyHandler {
    fun getEnergySideFor(): Direction? = null

    fun getAmountAsLong(side: Direction?): Long

    @Deprecated("Use 'getAmountAsLong(Direction?)' instead")
    override fun getAmountAsLong(): Long = getAmountAsLong(getEnergySideFor())

    fun getCapacityAsLong(side: Direction?): Long

    @Deprecated("Use 'getCapacityAsLong(Direction?)' instead")
    override fun getCapacityAsLong(): Long = getCapacityAsLong(getEnergySideFor())

    fun insert(amount: Int, transaction: TransactionContext, side: Direction?): Int

    @Deprecated("Use 'insert(Int, TransactionContext, Direction?)' instead")
    override fun insert(amount: Int, transaction: TransactionContext): Int = insert(amount, transaction, getEnergySideFor())

    fun extract(amount: Int, transaction: TransactionContext, side: Direction?): Int

    @Deprecated("Use 'extract(Int, TransactionContext, Direction?)' instead")
    override fun extract(amount: Int, transaction: TransactionContext): Int = extract(amount, transaction, getEnergySideFor())
}
