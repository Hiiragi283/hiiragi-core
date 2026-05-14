package hiiragi283.lib.transfer.fluid

import hiiragi283.lib.transfer.HTResourceHandler
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource

typealias FluidResourceHandler = ResourceHandler<FluidResource>

typealias HTFluidResourceHandler = HTResourceHandler<FluidResource>

fun FluidResourceHandler.getFluidStack(index: Int): FluidStack = this.getResource(index).toStack(this.getAmountAsInt(index))

fun FluidStack.toResourcePair(): Pair<FluidResource, Int> = FluidResource.of(this) to this.amount

fun FluidStackTemplate.toResourcePair(): Pair<FluidResource, Int> = FluidResource.of(this) to this.amount
