package hiiragi283.core.common.recipe.handler

import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.insert
import net.neoforged.neoforge.fluids.FluidStack

interface HTFluidOutputHandler : HTOutputHandler<FluidStack> {
    companion object {
        @JvmStatic
        fun single(tank: HTFluidTank): HTFluidOutputHandler = Single(tank)
    }

    private class Single(private val tank: HTFluidTank) : HTFluidOutputHandler {
        override fun canInsert(stack: FluidStack): Boolean = tank.insert(stack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).isEmpty

        override fun insert(stack: FluidStack) {
            tank.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }
}
