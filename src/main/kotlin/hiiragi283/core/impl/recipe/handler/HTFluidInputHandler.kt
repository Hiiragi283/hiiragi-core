package hiiragi283.core.impl.recipe.handler

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.getFluidStack
import net.neoforged.neoforge.fluids.FluidStack

class HTFluidInputHandler(private val tank: HTFluidTank) :
    HTInputHandler<FluidStack>,
    HTFluidTank by tank {
    override fun getStack(): FluidStack = this.getFluidStack()

    override fun consume(amount: Int) {
        if (amount > 0) {
            extract(amount, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        }
    }

    override fun getCapacity(): Int = tank.getCapacity()

    override fun isEmpty(): Boolean = tank.isEmpty()
}
