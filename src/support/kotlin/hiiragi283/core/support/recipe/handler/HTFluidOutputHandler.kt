package hiiragi283.core.support.recipe.handler

import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.insert
import hiiragi283.core.util.HTStackSlotHelper
import net.neoforged.neoforge.fluids.FluidStack

interface HTFluidOutputHandler : HTOutputHandler<FluidStack> {
    companion object {
        @JvmStatic
        fun single(tank: HTFluidTank): HTFluidOutputHandler = Single(tank)

        @JvmStatic
        fun multiple(vararg tanks: HTFluidTank): HTFluidOutputHandler = multiple(tanks.asSequence())

        @JvmStatic
        fun multiple(tanks: Iterable<HTFluidTank>): HTFluidOutputHandler = multiple(tanks.asSequence())

        @JvmStatic
        fun multiple(tanks: Sequence<HTFluidTank>): HTFluidOutputHandler = Multiple(tanks)
    }

    private class Single(private val tank: HTFluidTank) : HTFluidOutputHandler {
        override fun canInsert(stack: FluidStack): Boolean = tank.insert(stack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).isEmpty

        override fun insert(stack: FluidStack) {
            tank.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }

    private class Multiple(private val tanks: Sequence<HTFluidTank>) : HTFluidOutputHandler {
        override fun canInsert(stack: FluidStack): Boolean = HTStackSlotHelper.insertStacks(
            tanks,
            stack,
            HTStorageAction.SIMULATE,
            HTStorageAccess.INTERNAL,
        ) == 0

        override fun insert(stack: FluidStack) {
            HTStackSlotHelper.insertStacks(
                tanks,
                stack,
                HTStorageAction.EXECUTE,
                HTStorageAccess.INTERNAL,
            )
        }
    }
}
