package hiiragi283.lib.recipe.handler

import hiiragi283.lib.transfer.extractSelf
import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.transaction.TransactionContext

/**
 * [FluidStack]向けの[HTInputHandler]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTFluidInputHandler(private val handler: FluidResourceHandler, private val index: Int) : HTInputHandler<FluidStack> {
    override fun extract(amount: Int, transaction: TransactionContext): Result<Int> = runCatching { handler.extractSelf(index, amount, transaction) }
}
