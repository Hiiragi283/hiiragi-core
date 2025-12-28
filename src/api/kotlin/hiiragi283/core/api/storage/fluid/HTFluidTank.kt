package hiiragi283.core.api.storage.fluid

import hiiragi283.core.api.storage.resource.HTResourceSlot
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [HTFluidResourceType]向けの[HTResourceSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
interface HTFluidTank : HTResourceSlot<HTFluidResourceType> {
    //    Basic    //

    /**
     * [HTFluidResourceType]向けの[HTResourceSlot.Basic]の拡張クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.4.0
     */
    abstract class Basic :
        HTResourceSlot.Basic<HTFluidResourceType>(),
        HTFluidTank {
        fun setStack(stack: FluidStack) {
            setResource(stack.toResource())
            setAmount(stack.amount)
        }

        override fun toString(): String = "HTFluidTank(resource=${getResource()}, amount=${getAmount()}, capacity=${getCapacity()})"
    }
}
