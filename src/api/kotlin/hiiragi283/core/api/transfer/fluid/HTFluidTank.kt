package hiiragi283.core.api.transfer.fluid

import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.HTResourceSlot
import hiiragi283.core.api.transfer.HTResourceView
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

fun FluidStack.toResourcePair(): Pair<FluidResource, Int> = FluidResource.of(this) to this.amount

/**
 * [FluidResource]向けの[HTResourceView]のエイリアス
 */
typealias HTFluidView = HTResourceView<FluidResource>

/**
 * [FluidResource]向けの[HTResourceSlot]のエイリアス
 */
typealias HTFluidTank = HTResourceSlot<FluidResource>

val HTFluidView.stack: FluidStack get() = this.resource.toStack(this.amountAsInt)

fun HTFluidTank.insert(stack: FluidStack, transaction: TransactionContext, access: HTHandlerAccess): Int {
    val resource: FluidResource = FluidResource.of(stack)
    return when {
        resource.isEmpty -> 0
        else -> this.insert(resource, stack.amount, transaction, access)
    }
}

fun HTFluidTank.extract(stack: FluidStack, transaction: TransactionContext, access: HTHandlerAccess): Int {
    val resource: FluidResource = FluidResource.of(stack)
    return when {
        resource.isEmpty -> 0
        else -> this.extract(resource, stack.amount, transaction, access)
    }
}
