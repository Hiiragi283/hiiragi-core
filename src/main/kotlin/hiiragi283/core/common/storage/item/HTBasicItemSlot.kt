package hiiragi283.core.common.storage.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.storage.HTStorageValidators
import hiiragi283.core.impl.storage.item.HTItemStackResourceSlot
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see mekanism.common.inventory.slot.BasicInventorySlot
 */
open class HTBasicItemSlot(
    private val limit: Int,
    private val canExtract: BiPredicate<HTItemResourceType, HTStorageAccess>,
    private val canInsert: BiPredicate<HTItemResourceType, HTStorageAccess>,
    private val filter: Predicate<HTItemResourceType>,
    private val listener: HTContentListener?,
) : HTItemStackResourceSlot() {
    companion object {
        @JvmStatic
        fun create(
            listener: HTContentListener?,
            limit: Int = HTConst.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<HTItemResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            canInsert: BiPredicate<HTItemResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            filter: Predicate<HTItemResourceType> = HTStoragePredicates.alwaysTrue(),
        ): HTBasicItemSlot = HTBasicItemSlot(HTStorageValidators.validateLimit(limit), canExtract, canInsert, filter, listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            limit: Int = HTConst.ABSOLUTE_MAX_STACK_SIZE,
            canInsert: Predicate<HTItemResourceType> = HTStoragePredicates.alwaysTrue(),
            filter: Predicate<HTItemResourceType> = canInsert,
        ): HTBasicItemSlot = create(
            listener,
            limit,
            HTStoragePredicates.notExternal(),
            { stack: HTItemResourceType, _ -> canInsert.test(stack) },
            filter,
        )

        @JvmStatic
        fun output(listener: HTContentListener?): HTBasicItemSlot = create(
            listener,
            canInsert = HTStoragePredicates.internalOnly(),
        )
    }

    @JvmField
    protected var stack: ItemStack = ItemStack.EMPTY

    final override fun getStack(): ItemStack = stack.copy()

    override fun setStack(stack: ItemStack) {
        setStackUnchecked(stack, true)
    }

    override fun setStackInternal(stack: ItemStack) {
        setStackUnchecked(stack, false)
    }

    private fun setStackUnchecked(other: ItemStack, validate: Boolean) {
        val resource: HTItemResourceType? = getResourceFrom(other)
        if (resource == null) {
            if (this.stack.isEmpty) return
            this.stack = ItemStack.EMPTY
        } else if (!validate || isValid(resource)) {
            this.stack = other
        } else {
            error("Invalid stack for slot: $other")
        }
        onContentsChanged()
    }

    final override fun updateAmount(newAmount: Int) {
        stack.count = newAmount
    }

    final override fun isValid(resource: HTItemResourceType): Boolean = this.filter.test(resource)

    final override fun isStackValidForInsert(resource: HTItemResourceType, access: HTStorageAccess): Boolean = super.isStackValidForInsert(resource, access) && this.canInsert.test(resource, access)

    final override fun canStackExtract(resource: HTItemResourceType, access: HTStorageAccess): Boolean = super.canStackExtract(resource, access) && this.canExtract.test(resource, access)

    override fun getCapacity(resource: HTItemResourceType?): Int = if (resource == null) limit else minOf(limit, resource.toStack().maxStackSize)

    override fun serialize(output: HTValueOutput) {
        output.write(HTConst.ITEM, ItemStack.CODEC, this.stack)
    }

    override fun deserialize(input: HTValueInput) {
        input.read(HTConst.ITEM, ItemStack.CODEC)?.let(::setStackInternal)
    }

    final override fun onContentsChanged() {
        this.listener?.onContentsChanged()
    }
}
