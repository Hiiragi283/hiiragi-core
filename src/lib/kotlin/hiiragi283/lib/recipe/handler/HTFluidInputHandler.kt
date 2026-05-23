package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.HTHandlerAccess
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.fluid.getFluidStack
import hiiragi283.lib.transfer.useTransaction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTFluidInputHandler(private val tank: HTFluidTank) : HTInputHandler<FluidStack> {
    override fun getStack(): FluidStack = tank.getFluidStack()

    override fun extract(amount: Int, parent: TransactionContext?): Result<Int> = runCatching {
        useTransaction(parent) { transaction: Transaction -> tank.extract(tank.resource, amount, transaction, HTHandlerAccess.INTERNAL) }
    }
}
