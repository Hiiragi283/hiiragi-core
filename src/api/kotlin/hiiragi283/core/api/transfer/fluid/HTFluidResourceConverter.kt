package hiiragi283.core.api.transfer.fluid

import hiiragi283.core.api.transfer.HTResourceConverter
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource

object HTFluidResourceConverter : HTResourceConverter<FluidStack, FluidResource> {
    override fun getEmptyStack(): FluidStack = FluidStack.EMPTY

    override fun getResource(stack: FluidStack): FluidResource = FluidResource.of(stack)

    override fun getAmount(stack: FluidStack): Long = stack.amount.toLong()

    override fun setAmount(stack: FluidStack, amount: Long) {
        stack.amount = amount.toInt()
    }

    override fun copyStack(stack: FluidStack): FluidStack = stack.copy()

    override fun createStack(resource: FluidResource, amount: Int): FluidStack = resource.toStack(amount)
}
