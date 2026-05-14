package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import hiiragi283.lib.transfer.fluid.toResourcePair
import hiiragi283.lib.transfer.useTransaction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.RangedResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.Transaction
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTFluidOutputHandler : HTOutputHandler<FluidStack> {
    companion object {
        @JvmStatic
        fun single(handler: FluidResourceHandler, index: Int): HTFluidOutputHandler = Single(handler, index)

        @JvmStatic
        fun multiple(handler: FluidResourceHandler, range: IntRange): HTFluidOutputHandler = Multiple(handler, range)
    }

    private class Single(private val handler: FluidResourceHandler, private val index: Int) : HTFluidOutputHandler {
        override fun insert(stack: FluidStack, parent: TransactionContext?): Result<Int> = runCatching {
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            useTransaction(parent) { transaction: Transaction -> handler.insert(index, resource, amount, transaction) }
        }
    }

    private class Multiple(handler: FluidResourceHandler, range: IntRange) : HTFluidOutputHandler {
        private val handler: FluidResourceHandler = RangedResourceHandler.of(handler, range.first, range.last + 1)

        override fun insert(stack: FluidStack, parent: TransactionContext?): Result<Int> = runCatching {
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            useTransaction(parent) { transaction: Transaction -> handler.insert(resource, amount, transaction) }
        }
    }
}
