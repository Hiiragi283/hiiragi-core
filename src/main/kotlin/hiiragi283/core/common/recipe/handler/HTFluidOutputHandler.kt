package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.fluid.HTFluidTank
import hiiragi283.core.api.transfer.useTransaction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource

interface HTFluidOutputHandler : HTOutputHandler<FluidStack> {
    companion object {
        @JvmStatic
        fun single(slot: HTFluidTank): HTFluidOutputHandler = SingleN(slot)
    }

    private class SingleN(private val slot: HTFluidTank) : HTFluidOutputHandler {
        override fun canInsert(stack: FluidStack): Boolean = useTransaction {
            val amount: Int = stack.amount
            slot.insert(FluidResource.of(stack), amount, it, HTHandlerAccess.INTERNAL) == amount
        }

        override fun insert(stack: FluidStack) {
            useTransaction {
                slot.insert(FluidResource.of(stack), stack.amount, it, HTHandlerAccess.INTERNAL)
                it.commit()
            }
        }
    }
}
