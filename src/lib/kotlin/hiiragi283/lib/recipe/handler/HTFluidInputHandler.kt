package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.HTHandlerAccess
import hiiragi283.lib.transfer.fluid.HTFluidTank
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTFluidInputHandler(tank: HTFluidTank) :
    HTInputHandler<FluidStack>,
    HTFluidTank by tank {
    override fun extract(amount: Int, transaction: TransactionContext): Result<Int> = runCatching { this.extract(this.resource, amount, transaction, HTHandlerAccess.INTERNAL) }
}
