package hiiragi283.core.impl.storage.fluid

import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.storage.fluid.toStackOrEmpty
import hiiragi283.core.impl.storage.resource.HTStackResourceSlot
import net.neoforged.neoforge.fluids.FluidStack

abstract class HTFluidStackResourceSlot : HTStackResourceSlot<FluidStack, HTFluidResourceType>() {
    final override fun getResourceFrom(stack: FluidStack): HTFluidResourceType? = stack.toResource()

    final override fun getAmountFrom(stack: FluidStack): Int = stack.amount

    final override fun isSame(stack: FluidStack, resource: HTFluidResourceType): Boolean = stack.toResource() == resource

    final override fun createStack(resource: HTFluidResourceType?, amount: Int): FluidStack = resource.toStackOrEmpty(amount)
}
