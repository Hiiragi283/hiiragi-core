package hiiragi283.core.common.storage.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.api.stack.toImmutable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.item.HTItemSlot
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see mekanism.common.inventory.slot.BasicInventorySlot
 */
open class HTBasicItemSlot protected constructor(
    private val limit: Int,
    private val canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    private val canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    private val filter: Predicate<ImmutableItemStack>,
    private val listener: HTContentListener?,
) : HTItemSlot.Basic() {
    companion object {
        @JvmStatic
        private fun validateLimit(limit: Int): Int {
            check(limit >= 0) { "Limit must be non negative" }
            return limit
        }

        @JvmStatic
        private fun create(
            listener: HTContentListener?,
            limit: Int = HTConst.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            filter: Predicate<ImmutableItemStack> = HTStoragePredicates.alwaysTrue(),
        ): HTBasicItemSlot = HTBasicItemSlot(validateLimit(limit), canExtract, canInsert, filter, listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            limit: Int = HTConst.ABSOLUTE_MAX_STACK_SIZE,
            canInsert: Predicate<ImmutableItemStack> = HTStoragePredicates.alwaysTrue(),
            filter: Predicate<ImmutableItemStack> = canInsert,
        ): HTBasicItemSlot = create(
            listener,
            limit,
            HTStoragePredicates.notExternal(),
            { stack: ImmutableItemStack, _ -> canInsert.test(stack) },
            filter,
        )
    }

    protected constructor(
        limit: Int,
        canExtract: Predicate<ImmutableItemStack>,
        canInsert: Predicate<ImmutableItemStack>,
        filter: Predicate<ImmutableItemStack>,
        listener: HTContentListener?,
    ) : this(
        limit,
        { stack: ImmutableItemStack, access: HTStorageAccess -> access == HTStorageAccess.MANUAL || canExtract.test(stack) },
        { stack: ImmutableItemStack, _: HTStorageAccess -> canInsert.test(stack) },
        filter,
        listener,
    )

    @JvmField
    protected var stack: ItemStack = ItemStack.EMPTY

    override fun getStack(): ImmutableItemStack? = this.stack.toImmutable()

    override fun getCapacity(stack: ImmutableItemStack?): Int = HTItemSlot.getMaxStackSize(stack, this.limit)

    final override fun isValid(stack: ImmutableItemStack): Boolean = this.filter.test(stack)

    final override fun isStackValidForInsert(stack: ImmutableItemStack, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(stack, access) && this.canInsert.test(stack, access)

    final override fun canStackExtract(stack: ImmutableItemStack, access: HTStorageAccess): Boolean =
        super.canStackExtract(stack, access) && this.canExtract.test(stack, access)

    override fun serialize(output: HTValueOutput) {
        output.store(HTConst.ITEM, ImmutableItemStack.CODEC, getStack())
    }

    override fun deserialize(input: HTValueInput) {
        input.readAndSet(HTConst.ITEM, ImmutableItemStack.CODEC, ::setStackUnchecked)
    }

    final override fun onContentsChanged() {
        this.listener?.onContentsChanged()
    }

    override fun setStack(stack: ImmutableItemStack?) {
        setStackUnchecked(stack, true)
    }

    fun setStackUnchecked(stack: ImmutableItemStack?, validate: Boolean = false) {
        if (stack == null) {
            if (this.getStack() == null) return
            this.stack = ItemStack.EMPTY
        } else if (!validate || isValid(stack)) {
            this.stack = stack.unwrap()
        } else {
            error("Invalid stack for slot: $stack ${stack.componentsPatch()}")
        }
        onContentsChanged()
    }

    override fun updateAmount(amount: Int) {
        this.stack.count = amount
    }
}
