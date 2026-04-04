package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.testOnlyType
import hiiragi283.core.api.transfer.FluidResourceHandler
import hiiragi283.core.api.transfer.getStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTFluidInputHandler(private val handler: FluidResourceHandler, private val index: Int) : HTInputHandler<SizedFluidIngredient> {
    val stack: FluidStack get() = handler.getStack(index)

    override fun getMatchingAmount(ingredient: SizedFluidIngredient): Int = when {
        ingredient.testOnlyType(handler.getResource(index)) -> ingredient.amount()
        else -> 0
    }

    override fun consume(amount: Int, transaction: TransactionContext) {
        if (amount > 0) {
            val resource: FluidResource = handler.getResource(index)
            if (resource.isEmpty) return
            handler.extract(index, resource, amount, transaction)
        }
    }
}
