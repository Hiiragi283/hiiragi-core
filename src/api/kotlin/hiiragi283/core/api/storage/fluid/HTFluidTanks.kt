package hiiragi283.core.api.storage.fluid

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.resource.HTResourceView
import net.neoforged.neoforge.fluids.FluidStack

typealias HTFluidView = HTResourceView<HTFluidResourceType>

fun HTFluidView.getFluidStack(): FluidStack = this.getResource()?.toStack(this.getAmount()) ?: FluidStack.EMPTY

fun HTFluidTank.insert(stack: FluidStack, action: HTStorageAction, access: HTStorageAccess): FluidStack {
    val remainder: Int = this.insert(stack.toResource(), stack.amount, action, access)
    return stack.copyWithAmount(remainder)
}

fun HTFluidTank.extractFluid(stack: FluidStack, action: HTStorageAction, access: HTStorageAccess): FluidStack =
    this.extract(stack.toResource(), stack.amount, action, access).let(stack::copyWithAmount)

fun HTFluidTank.extractFluid(amount: Int, action: HTStorageAction, access: HTStorageAccess): FluidStack {
    val resourceIn: HTFluidResourceType = this.getResource() ?: return FluidStack.EMPTY
    return this.extract(amount, action, access).let(resourceIn::toStack)
}
