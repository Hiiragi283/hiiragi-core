package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.StrictResourceHandler
import hiiragi283.core.api.transfer.StrictResourceSlot
import hiiragi283.core.api.transfer.useTransaction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource

interface HTFluidOutputHandler : HTOutputHandler<FluidStack> {
    companion object {
        @JvmStatic
        fun single(handler: StrictResourceHandler<FluidResource>, index: Int): HTFluidOutputHandler =
            single(StrictResourceSlot.of(handler, index))

        @JvmStatic
        fun single(slot: StrictResourceSlot<FluidResource>): HTFluidOutputHandler = SingleN(slot)
    }

    private class SingleN(private val slot: StrictResourceSlot<FluidResource>) : HTFluidOutputHandler {
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
