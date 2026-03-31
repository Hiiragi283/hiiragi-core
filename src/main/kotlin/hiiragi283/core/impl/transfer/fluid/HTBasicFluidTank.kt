package hiiragi283.core.impl.transfer.fluid

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.HTTransferPredicates
import hiiragi283.core.impl.transfer.HTBasicResourceSlot
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.function.BiPredicate
import java.util.function.Predicate

open class HTBasicFluidTank(
    private val capacity: Int,
    private val canExtract: BiPredicate<FluidResource, HTHandlerAccess>,
    private val canInsert: BiPredicate<FluidResource, HTHandlerAccess>,
    private val filter: Predicate<FluidResource>,
    private val listener: HTContentListener?,
) : HTBasicResourceSlot<FluidResource>() {
    companion object {
        @JvmStatic
        private fun validateCapacity(capacity: Int): Int {
            check(capacity >= 0) { "Capacity must be non negative" }
            return capacity
        }

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: Int,
            canExtract: BiPredicate<FluidResource, HTHandlerAccess> = HTTransferPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<FluidResource, HTHandlerAccess> = HTTransferPredicates.alwaysTrueBi(),
            filter: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicFluidTank = HTBasicFluidTank(validateCapacity(capacity), canExtract, canInsert, filter, listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: Int,
            canInsert: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
            filter: Predicate<FluidResource> = canInsert,
        ): HTBasicFluidTank = create(
            listener,
            capacity,
            HTTransferPredicates.notExternal(),
            { stack: FluidResource, _ -> canInsert.test(stack) },
            filter,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Int): HTBasicFluidTank = create(
            listener,
            capacity,
            canInsert = HTTransferPredicates.internalOnly(),
        )
    }

    @JvmField
    protected var stack: FluidStack = FluidStack.EMPTY

    private val journal = StackJournal()

    fun setResourceUnchecked(resource: FluidResource, validate: Boolean = false) {
        if (resource.isEmpty) {
            if (this.resource.isEmpty) return
            this.stack = FluidStack.EMPTY
        } else if (!validate || isValid(resource)) {
            this.stack = resource.toStack(this.stack.amount)
        } else {
            error("Invalid stack for slot: $resource")
        }
        onContentsChanged()
    }

    //    HTBasicResourceSlot    //

    override fun updateSnapshot(transaction: TransactionContext) {
        journal.updateSnapshots(transaction)
    }

    override var resource: FluidResource
        get() = FluidResource.of(stack)
        set(value) {
            setResourceUnchecked(value, true)
        }
    override var amountAsLong: Long
        get() = stack.amount.toLong()
        set(value) {
            stack.amount = value.toInt()
        }

    final override fun isValid(resource: FluidResource): Boolean = filter.test(resource)

    final override fun isStackValidForInsert(resource: FluidResource, access: HTHandlerAccess): Boolean =
        super.isStackValidForInsert(resource, access) && canInsert.test(resource, access)

    final override fun canStackExtract(resource: FluidResource, access: HTHandlerAccess): Boolean =
        super.canStackExtract(resource, access) && canExtract.test(resource, access)

    override fun getCapacityAsLong(resource: FluidResource): Long = capacity.toLong()

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun serialize(output: ValueOutput) {
        output.store(HTConst.ITEM, FluidStack.OPTIONAL_CODEC, stack)
    }

    override fun deserialize(input: ValueInput) {
        input.read(HTConst.ITEM, FluidStack.OPTIONAL_CODEC).ifPresent(::stack::set)
    }

    private inner class StackJournal : SnapshotJournal<FluidStack>() {
        override fun createSnapshot(): FluidStack = this@HTBasicFluidTank.stack.copy()

        override fun revertToSnapshot(shapshot: FluidStack) {
            this@HTBasicFluidTank.stack = shapshot
        }

        override fun onRootCommit(originalState: FluidStack) {
            this@HTBasicFluidTank.onContentsChanged()
        }
    }
}
