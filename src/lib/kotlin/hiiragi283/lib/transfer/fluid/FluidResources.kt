package hiiragi283.lib.transfer.fluid

import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.HTResourceView
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource

//    FluidResource    //

fun FluidStack.toResourcePair(): Pair<FluidResource, Int> = FluidResource.of(this) to this.amount

fun FluidStackTemplate.toResourcePair(): Pair<FluidResource, Int> = FluidResource.of(this) to this.amount

//    ResourceHandler    //

typealias FluidResourceHandler = ResourceHandler<FluidResource>

fun FluidResourceHandler.getFluidStack(index: Int): FluidStack = this.getResource(index).toStack(this.getAmountAsInt(index))

typealias HTFluidView = HTResourceView<FluidResource>

fun HTFluidView.getFluidStack(): FluidStack = this.resource.toStack(this.amountAsInt)

typealias HTFluidTank = HTResourceSlot<FluidResource>
