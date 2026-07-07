package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import hiiragi283.lib.transfer.fluid.toResourcePair
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * [FluidStack]向けの[HTOutputHandler]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTFluidOutputHandler : HTOutputHandler<FluidStack> {
    companion object {
        @JvmStatic
        fun single(handler: FluidResourceHandler, index: Int): HTFluidOutputHandler = Single(handler, index)

        @JvmStatic
        fun multiple(handler: FluidResourceHandler): HTFluidOutputHandler = Multiple(handler)
    }

    /**
     * 単一のスロットに対する[HTFluidOutputHandler]の実装クラスです。
     */
    private class Single(private val handler: FluidResourceHandler, private val index: Int) : HTFluidOutputHandler {
        override fun insert(stack: FluidStack, transaction: TransactionContext): Result<Int> = runCatching {
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            handler.insert(index, resource, amount, transaction)
        }
    }

    /**
     * 複数のスロットに対する[HTFluidOutputHandler]の実装クラスです。
     */
    private class Multiple(private val handler: FluidResourceHandler) : HTFluidOutputHandler {
        override fun insert(stack: FluidStack, transaction: TransactionContext): Result<Int> = runCatching {
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            handler.insert(resource, amount, transaction)
        }
    }
}
