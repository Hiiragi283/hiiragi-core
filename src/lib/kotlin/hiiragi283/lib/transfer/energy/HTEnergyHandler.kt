package hiiragi283.lib.transfer.energy

import hiiragi283.lib.transfer.HTHandlerAccess
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTEnergyHandler : EnergyHandler {
    fun insert(amount: Int, access: HTHandlerAccess, transaction: TransactionContext): Int

    override fun insert(amount: Int, transaction: TransactionContext): Int = insert(amount, HTHandlerAccess.EXTERNAL, transaction)

    fun extract(amount: Int, access: HTHandlerAccess, transaction: TransactionContext): Int

    override fun extract(amount: Int, transaction: TransactionContext): Int = extract(amount, HTHandlerAccess.EXTERNAL, transaction)
}
