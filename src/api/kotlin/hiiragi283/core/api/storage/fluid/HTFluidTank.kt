package hiiragi283.core.api.storage.fluid

import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.storage.stack.HTStackSlot
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [ImmutableFluidStack]向けの[HTStackSlot]の拡張インターフェース
 * @see mekanism.api.fluid.IExtendedFluidTank
 */
interface HTFluidTank : HTStackSlot<ImmutableFluidStack> {
    override fun isSameStack(other: ImmutableFluidStack?): Boolean = FluidStack.isSameFluidSameComponents(
        this.getFluidStack(),
        other?.unwrap() ?: FluidStack.EMPTY,
    )

    //    Basic    //

    /**
     * [ImmutableFluidStack]向けの[HTStackSlot.Basic]の拡張クラス
     */
    abstract class Basic :
        HTStackSlot.Basic<ImmutableFluidStack>(),
        HTFluidTank {
        override fun toString(): String = "HTFluidTank(stack=${getStack()}, capacity=${getCapacity()})"
    }
}
