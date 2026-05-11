package hiiragi283.lib.transfer.item

import com.google.common.base.Predicates
import hiiragi283.lib.HTConstants
import hiiragi283.lib.util.HTSimpleProperty
import java.util.function.Predicate
import kotlin.properties.ReadWriteProperty
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.item.ItemResource

open class HTBasicItemSlot(
    property: ReadWriteProperty<Any?, ItemStack>,
    private val limit: Int,
    private val filter: Predicate<ItemResource>,
    private val listener: Runnable?,
) : HTItemStackResourceSlot() {
    companion object {
        @JvmStatic
        fun create(
            listener: Runnable?,
            limit: Int = Item.ABSOLUTE_MAX_STACK_SIZE,
            filter: Predicate<ItemResource> = Predicates.alwaysTrue(),
            property: ReadWriteProperty<Any?, ItemStack> = HTSimpleProperty(ItemStack.EMPTY),
        ): HTBasicItemSlot {
            TransferPreconditions.checkNonNegative(limit)
            return HTBasicItemSlot(property, limit, filter, listener)
        }
    }

    private var stack: ItemStack by property

    override fun getStack(): ItemStack = stack.copy()

    override fun setStack(stack: ItemStack) {
        setStackUnchecked(stack, true)
    }

    override fun setStackInternal(stack: ItemStack) {
        setStackUnchecked(stack, false)
    }

    override fun onContentsChanged() {
        listener?.run()
    }

    private fun setStackUnchecked(other: ItemStack, validate: Boolean) {
        val resource: ItemResource = getResourceFrom(other)
        if (resource.isEmpty) {
            if (this.stack.isEmpty) return
            this.stack = ItemStack.EMPTY
        } else if (!validate || isValid(resource)) {
            this.stack = other
        } else {
            error("Invalid stack for slot: $other")
        }
    }

    override fun updateAmount(newAmount: Int) {
        stack.count = newAmount
    }

    override fun isValid(resource: ItemResource): Boolean = this.filter.test(resource)

    override fun getCapacityAsLong(resource: ItemResource): Long {
        val capacity: Int = if (resource.isEmpty) limit else minOf(limit, resource.toStack().maxStackSize)
        return capacity.toLong()
    }

    override fun serialize(output: ValueOutput) {
        output.store(HTConstants.ITEM, ItemStack.CODEC, this.stack)
    }

    override fun deserialize(input: ValueInput) {
        input.read(HTConstants.ITEM, ItemStack.CODEC).ifPresent(::setStackInternal)
    }
}
