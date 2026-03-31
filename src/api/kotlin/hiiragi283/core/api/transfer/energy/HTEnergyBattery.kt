package hiiragi283.core.api.transfer.energy

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.transfer.HTHandlerAccess
import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTEnergyBattery :
    EnergyHandler,
    HTContentListener,
    ValueIOSerializable {
    fun insert(amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int

    @Deprecated("Use 'insert(Int, TransactionContext, HTHandlerAccess)' instead")
    override fun insert(amount: Int, transaction: TransactionContext): Int = insert(amount, transaction, HTHandlerAccess.EXTERNAL)

    fun extract(amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int

    @Deprecated("Use 'extract(Int, TransactionContext, HTHandlerAccess)' instead")
    override fun extract(amount: Int, transaction: TransactionContext): Int = extract(amount, transaction, HTHandlerAccess.EXTERNAL)
}
