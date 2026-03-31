package hiiragi283.core.api.transfer.energy

import hiiragi283.core.api.transfer.HTHandlerAccess
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTEnergyHandler : SidedEnergyHandler {
    fun hasEnergyHandler(): Boolean = true

    fun insert(
        amount: Int,
        transaction: TransactionContext,
        side: Direction?,
        access: HTHandlerAccess,
    ): Int

    fun extract(
        amount: Int,
        transaction: TransactionContext,
        side: Direction?,
        access: HTHandlerAccess,
    ): Int

    override fun insert(amount: Int, transaction: TransactionContext, side: Direction?): Int =
        insert(amount, transaction, side, HTHandlerAccess.forHandler(side))

    override fun extract(amount: Int, transaction: TransactionContext, side: Direction?): Int =
        extract(amount, transaction, side, HTHandlerAccess.forHandler(side))
}
