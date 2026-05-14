package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import hiiragi283.lib.transfer.fluid.getFluidStack
import hiiragi283.lib.transfer.useTransaction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTFluidInputHandler(private val handler: FluidResourceHandler, private val index: Int) : HTInputHandler<FluidStack> {
    override fun getStack(): FluidStack = handler.getFluidStack(index)

    override fun consume(amount: Int, parent: TransactionContext?) {
        if (amount > 0) {
            val resourceIn: FluidResource = handler.getResource(index)
            if (resourceIn.isEmpty) return
            useTransaction(parent) { transaction: Transaction ->
                handler.extract(index, resourceIn, amount, transaction)
                transaction.commit()
            }
        }
    }
}
