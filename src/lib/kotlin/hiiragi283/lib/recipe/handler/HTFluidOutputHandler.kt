package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.HTHandlerAccess
import hiiragi283.lib.transfer.HTResourceHandler
import hiiragi283.lib.transfer.fluid.HTFluidTank
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
        fun single(tank: HTFluidTank): HTFluidOutputHandler = Single(tank)

        @JvmStatic
        fun multiple(vararg tanks: HTFluidTank): HTFluidOutputHandler = Multiple(tanks.toList())

        @JvmStatic
        fun multiple(tanks: List<HTFluidTank>): HTFluidOutputHandler = when (tanks.size) {
            1 -> single(tanks.first())
            else -> Multiple(tanks)
        }
    }

    /**
     * 単一の[HTFluidTank]に対する[HTFluidOutputHandler]の実装クラスです。
     */
    private class Single(private val tank: HTFluidTank) : HTFluidOutputHandler {
        override fun insert(stack: FluidStack, transaction: TransactionContext): Result<Int> = runCatching {
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            tank.insert(resource, amount, transaction, HTHandlerAccess.INTERNAL)
        }
    }

    /**
     * 複数の[HTFluidTank]に対する[HTFluidOutputHandler]の実装クラスです。
     */
    private class Multiple(tanks: List<HTFluidTank>) : HTFluidOutputHandler {
        private val handler = HTResourceHandler { tanks }

        override fun insert(stack: FluidStack, transaction: TransactionContext): Result<Int> = runCatching {
            val (resource: FluidResource, amount: Int) = stack.toResourcePair()
            handler.insert(resource, amount, transaction, HTHandlerAccess.INTERNAL)
        }
    }
}
