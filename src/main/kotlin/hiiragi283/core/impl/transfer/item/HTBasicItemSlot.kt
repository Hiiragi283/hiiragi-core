package hiiragi283.core.impl.transfer.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.HTTransferPredicates
import hiiragi283.core.impl.transfer.HTBasicResourceSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.function.BiPredicate
import java.util.function.Predicate

open class HTBasicItemSlot(
    private val limit: Int,
    private val canExtract: BiPredicate<ItemResource, HTHandlerAccess>,
    private val canInsert: BiPredicate<ItemResource, HTHandlerAccess>,
    private val filter: Predicate<ItemResource>,
    private val listener: HTContentListener?,
) : HTBasicResourceSlot<ItemResource>() {
    companion object {
        @JvmStatic
        private fun validateLimit(limit: Int): Int {
            check(limit >= 0) { "Limit must be non negative" }
            return limit
        }

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            limit: Int = Item.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<ItemResource, HTHandlerAccess> = HTTransferPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ItemResource, HTHandlerAccess> = HTTransferPredicates.alwaysTrueBi(),
            filter: Predicate<ItemResource> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicItemSlot = HTBasicItemSlot(validateLimit(limit), canExtract, canInsert, filter, listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            limit: Int = Item.ABSOLUTE_MAX_STACK_SIZE,
            canInsert: Predicate<ItemResource> = HTTransferPredicates.alwaysTrue(),
            filter: Predicate<ItemResource> = canInsert,
        ): HTBasicItemSlot = create(
            listener,
            limit,
            HTTransferPredicates.notExternal(),
            { stack: ItemResource, _ -> canInsert.test(stack) },
            filter,
        )

        @JvmStatic
        fun output(listener: HTContentListener?): HTBasicItemSlot = create(
            listener,
            canInsert = HTTransferPredicates.internalOnly(),
        )
    }

    @JvmField
    protected var stack: ItemStack = ItemStack.EMPTY

    private val journal = StackJournal()

    fun setResourceUnchecked(resource: ItemResource, validate: Boolean = false) {
        if (resource.isEmpty) {
            if (this.resource.isEmpty) return
            this.stack = ItemStack.EMPTY
        } else if (!validate || isValid(resource)) {
            this.stack = resource.toStack(this.stack.count)
        } else {
            error("Invalid stack for slot: $resource")
        }
        onContentsChanged()
    }

    //    HTBasicResourceSlot    //

    override fun updateSnapshot(transaction: TransactionContext) {
        journal.updateSnapshots(transaction)
    }

    override var resource: ItemResource
        get() = ItemResource.of(stack)
        set(value) {
            setResourceUnchecked(value, true)
        }
    override var amountAsLong: Long
        get() = stack.count.toLong()
        set(value) {
            stack.count = value.toInt()
        }

    final override fun isValid(resource: ItemResource): Boolean = filter.test(resource)

    final override fun isStackValidForInsert(resource: ItemResource, access: HTHandlerAccess): Boolean =
        super.isStackValidForInsert(resource, access) && canInsert.test(resource, access)

    final override fun canStackExtract(resource: ItemResource, access: HTHandlerAccess): Boolean =
        super.canStackExtract(resource, access) && canExtract.test(resource, access)

    override fun getCapacityAsLong(resource: ItemResource): Long = when {
        resource.isEmpty -> limit
        else -> minOf(resource.maxStackSize, limit)
    }.toLong()

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun serialize(output: ValueOutput) {
        output.store(HTConst.ITEM, ItemStack.OPTIONAL_CODEC, stack)
    }

    override fun deserialize(input: ValueInput) {
        input.read(HTConst.ITEM, ItemStack.OPTIONAL_CODEC).ifPresent(::stack::set)
    }

    private inner class StackJournal : SnapshotJournal<ItemStack>() {
        override fun createSnapshot(): ItemStack = this@HTBasicItemSlot.stack.copy()

        override fun revertToSnapshot(shapshot: ItemStack) {
            this@HTBasicItemSlot.stack = shapshot
        }

        override fun onRootCommit(originalState: ItemStack) {
            this@HTBasicItemSlot.onContentsChanged()
        }
    }
}
