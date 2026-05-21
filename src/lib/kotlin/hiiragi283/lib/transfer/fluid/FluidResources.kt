package hiiragi283.lib.transfer.fluid

import hiiragi283.lib.transfer.HTResourceHandler
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.resource.ResourceStack

//    FluidResource    //

fun FluidStack.toResourcePair(): Pair<FluidResource, Int> = FluidResource.of(this) to this.amount

fun FluidStackTemplate.toResourcePair(): Pair<FluidResource, Int> = FluidResource.of(this) to this.amount

typealias FluidResourceStack = ResourceStack<FluidResource>

fun FluidResourceStack.toStack(): FluidStack = when {
    this.isEmpty -> FluidStack.EMPTY
    else -> this.resource().toStack(this.amount())
}

fun FluidStack.toResourceStack(): FluidResourceStack = ResourceStack(FluidResource.of(this), this.amount)

//    ResourceHandler    //

typealias FluidResourceHandler = ResourceHandler<FluidResource>

typealias HTFluidResourceHandler = HTResourceHandler<FluidResource>

fun FluidResourceHandler.getFluidStack(index: Int): FluidStack = this.getResource(index).toStack(this.getAmountAsInt(index))
