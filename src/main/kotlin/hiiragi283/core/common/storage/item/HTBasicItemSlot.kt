package hiiragi283.core.common.storage.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.api.storage.item.getItemStack
import hiiragi283.core.api.storage.item.toResource
import net.minecraft.world.item.ItemStack
import java.util.function.BiPredicate
import java.util.function.Predicate

open class HTBasicItemSlot protected constructor(
    private val limit: Int,
    private val canExtract: BiPredicate<HTItemResourceType, HTStorageAccess>,
    private val canInsert: BiPredicate<HTItemResourceType, HTStorageAccess>,
    private val filter: Predicate<HTItemResourceType>,
    private val listener: HTContentListener?,
) : HTItemSlot.Basic() {
    companion object {
        @JvmStatic
        private fun validateLimit(limit: Int): Int {
            check(limit >= 0) { "Limit must be non negative" }
            return limit
        }

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            limit: Int = HTConst.ABSOLUTE_MAX_STACK_SIZE,
            canExtract: BiPredicate<HTItemResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            canInsert: BiPredicate<HTItemResourceType, HTStorageAccess> = HTStoragePredicates.alwaysTrueBi(),
            filter: Predicate<HTItemResourceType> = HTStoragePredicates.alwaysTrue(),
        ): HTBasicItemSlot = HTBasicItemSlot(validateLimit(limit), canExtract, canInsert, filter, listener)

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

    override fun setResource(resource: HTItemResourceType?) {
        setResourceUnchecked(resource, true)
    }

    fun setResourceUnchecked(resource: HTItemResourceType?, validate: Boolean = false) {
        if (resource == null) {
            if (this.getResource() == null) return
            this.stack = ItemStack.EMPTY
        } else if (!validate || isValid(resource)) {
            this.stack = resource.toStack(stack.count)
        } else {
            error("Invalid stack for slot: $resource")
        }
        onContentsChanged()
    }

    final override fun setAmount(amount: Int) {
        stack.count = amount
    }

    final override fun isValid(resource: HTItemResourceType): Boolean = this.filter.test(resource)

    final override fun isStackValidForInsert(resource: HTItemResourceType, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(resource, access) && this.canInsert.test(resource, access)

    final override fun canStackExtract(resource: HTItemResourceType, access: HTStorageAccess): Boolean =
        super.canStackExtract(resource, access) && this.canExtract.test(resource, access)

    override fun getResource(): HTItemResourceType? = stack.toResource()

    override fun getCapacity(resource: HTItemResourceType?): Int = HTItemSlot.getMaxStackSize(resource, limit)

    override fun getAmount(): Int = stack.count

    override fun serialize(output: HTValueOutput) {
        output.store(HTConst.ITEM, VanillaBiCodecs.ITEM_STACK, getItemStack())
    }

    override fun deserialize(input: HTValueInput) {
        (input.read(HTConst.ITEM, VanillaBiCodecs.ITEM_STACK) ?: ItemStack.EMPTY).let(::setStack)
    }

    final override fun onContentsChanged() {
        this.listener?.onContentsChanged()
    }
}
