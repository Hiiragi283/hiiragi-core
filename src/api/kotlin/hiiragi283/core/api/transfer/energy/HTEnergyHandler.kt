package hiiragi283.core.api.transfer.energy

import hiiragi283.core.api.transfer.HTHandlerAccess
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTEnergyHandler : EnergyHandler {
    fun insert(amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int

    fun extract(amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int

    override fun insert(amount: Int, transaction: TransactionContext): Int = insert(amount, transaction, HTHandlerAccess.EXTERNAL)

    override fun extract(amount: Int, transaction: TransactionContext): Int = extract(amount, transaction, HTHandlerAccess.EXTERNAL)
}
