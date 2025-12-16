package hiiragi283.core.common.storage.item

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandlerModifiable
import kotlin.reflect.KMutableProperty0

open class HTSingleItemHandler(private var property: KMutableProperty0<ItemStack>) :
    IItemHandlerModifiable,
    HTContentListener {
    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        property.set(stack.copy())
    }

    override fun getSlots(): Int = 1

    override fun getStackInSlot(slot: Int): ItemStack = property.get().copy()

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (stack.isEmpty) return ItemStack.EMPTY
        if (!isItemValid(slot, stack)) return stack
        val existing: ItemStack = getStackInSlot(0)
        var limit: Int = getSlotLimit(slot)
        if (!existing.isEmpty) {
            if (!ItemStack.isSameItemSameComponents(stack, existing)) {
                return stack
            }
            limit -= existing.count
        }
        if (limit <= 0) return stack
        val reachedLimit: Boolean = stack.count > limit
        if (!simulate) {
            if (existing.isEmpty) {
                this.property.set(
                    when {
                        reachedLimit -> stack.copyWithCount(limit)
                        else -> stack
                    },
                )
            } else {
                existing.grow(
                    when {
                        reachedLimit -> limit
                        else -> stack.count
                    },
                )
            }
            onContentsChanged()
        }
        return when {
            reachedLimit -> stack.copyWithCount(stack.count - limit)
            else -> ItemStack.EMPTY
        }
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getSlotLimit(slot: Int): Int = HTConst.ABSOLUTE_MAX_STACK_SIZE

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = true

    override fun onContentsChanged() {}
}
