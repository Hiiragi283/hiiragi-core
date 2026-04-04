package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.transfer.FluidResourceHandler
import hiiragi283.core.api.transfer.fluid.toResourcePair
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTFluidOutputHandler : HTOutputHandler<FluidStack> {
    companion object {
        @JvmStatic
        fun single(handler: FluidResourceHandler, index: Int): HTFluidOutputHandler = Single(handler, index)

        @JvmStatic
        fun multiple(handler: FluidResourceHandler, indices: IntRange): HTFluidOutputHandler = multiple(handler, indices.toList())

        @JvmStatic
        fun multiple(handler: FluidResourceHandler, indices: Collection<Int>): HTFluidOutputHandler =
            multiple(handler, indices.toIntArray())

        @JvmStatic
        fun multiple(handler: FluidResourceHandler, indices: IntArray): HTFluidOutputHandler = Multiple(handler, indices)
    }

    override fun getResultAmount(stack: FluidStack): Int = stack.amount

    private class Single(private val handler: FluidResourceHandler, private val index: Int) : HTFluidOutputHandler {
        override fun insert(stack: FluidStack, transaction: TransactionContext): Int {
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            return handler.insert(index, resource, amount, transaction)
        }
    }

    private class Multiple(private val handler: FluidResourceHandler, private val indices: IntArray) : HTFluidOutputHandler {
        override fun insert(stack: FluidStack, transaction: TransactionContext): Int {
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()

            var inserted = 0
            for (i: Int in indices) {
                inserted += handler.insert(i, resource, amount - inserted, transaction)
                if (inserted == amount) break
            }
            return inserted
        }
    }
}
