package hiiragi283.core.api.transfer.fluid

import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.StrictResourceHandler
import net.minecraft.core.NonNullList
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

abstract class StrictFluidStacksResourceHandler :
    FluidStacksResourceHandler,
    StrictResourceHandler<FluidResource> {
    constructor(size: Int, capacity: Int) : super(size, capacity)

    constructor(stacks: NonNullList<FluidStack>, capacity: Int) : super(stacks, capacity)

    protected abstract fun canInsert(index: Int, access: HTHandlerAccess): Boolean

    protected abstract fun canExtract(index: Int, access: HTHandlerAccess): Boolean

    override fun insert(
        index: Int,
        resource: FluidResource,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int = when {
        canInsert(index, access) -> super<FluidStacksResourceHandler>.insert(index, resource, amount, transaction)
        else -> 0
    }

    override fun extract(
        index: Int,
        resource: FluidResource,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int = when {
        canExtract(index, access) -> super<FluidStacksResourceHandler>.extract(index, resource, amount, transaction)
        else -> 0
    }

    @Deprecated("Use `insert(Int, T, Int, TransactionContext, HTHandlerAccess)` instead")
    @Suppress("DEPRECATION")
    final override fun insert(
        index: Int,
        resource: FluidResource,
        amount: Int,
        transaction: TransactionContext,
    ): Int = super<StrictResourceHandler>.insert(index, resource, amount, transaction)

    @Deprecated("Use `extract(Int, T, Int, TransactionContext, HTHandlerAccess)` instead")
    @Suppress("DEPRECATION")
    final override fun extract(
        index: Int,
        resource: FluidResource,
        amount: Int,
        transaction: TransactionContext,
    ): Int = super<StrictResourceHandler>.extract(index, resource, amount, transaction)

    fun insertStack(
        index: Int,
        stack: FluidStack,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int = this.insert(index, FluidResource.of(stack), stack.amount, transaction, access)

    fun extractStack(
        index: Int,
        stack: FluidStack,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int = this.extract(index, FluidResource.of(stack), stack.amount, transaction, access)
}
