package hiiragi283.lib.transfer.energy

import hiiragi283.lib.transfer.HTHandlerAccess
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTEnergyHandler : EnergyHandler {
    fun insert(amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int

    override fun insert(amount: Int, transaction: TransactionContext): Int = insert(amount, transaction, HTHandlerAccess.EXTERNAL)

    fun extract(amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int

    override fun extract(amount: Int, transaction: TransactionContext): Int = extract(amount, transaction, HTHandlerAccess.EXTERNAL)
}
