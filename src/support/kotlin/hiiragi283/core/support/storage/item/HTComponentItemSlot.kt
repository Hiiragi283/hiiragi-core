package hiiragi283.core.support.storage.item

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.item.HTItemResourceType
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents

/**
 * @see net.neoforged.neoforge.items.ComponentItemHandler
 */
class HTComponentItemSlot(
    private val container: ItemStack,
    private val size: Int,
    private val index: Int,
    private val limit: Int,
    private val canExtract: BiPredicate<HTItemResourceType, HTStorageAccess>,
    private val canInsert: BiPredicate<HTItemResourceType, HTStorageAccess>,
    private val filter: Predicate<HTItemResourceType>,
) : HTItemStackResourceSlot(),
    HTContentListener by HTContentListener.NOTHING,
    HTValueSerializable by HTValueSerializable.NOTHING {
    private fun createNewList(): NonNullList<ItemStack> = NonNullList.withSize(size, ItemStack.EMPTY)

    private fun createContents(list: List<ItemStack>): ItemContainerContents = ItemContainerContents.fromItems(list)

    private fun getContents(): ItemContainerContents = container.getOrDefault(DataComponents.CONTAINER, createContents(createNewList()))

    //    HTItemStackResourceSlot    //

    override fun getStack(): ItemStack = getContents().getStackInSlot(index)

    override fun setStack(stack: ItemStack) {
        setStackInternal(stack)
    }

    override fun setStackInternal(stack: ItemStack) {
        val list: NonNullList<ItemStack> = createNewList()
        getContents().copyInto(list)
        // val oldStack = list.get(index)
        list[index] = stack
        if (list.all(ItemStack::isEmpty)) {
            container.remove(DataComponents.CONTAINER)
        } else {
            container.set(DataComponents.CONTAINER, createContents(list))
        }
    }

    override fun updateAmount(newAmount: Int) {
        val stackIn: ItemStack = getStack()
        stackIn.count = newAmount
        setStack(stackIn)
    }

    override fun isValid(resource: HTItemResourceType): Boolean = this.filter.test(resource)

    override fun isStackValidForInsert(resource: HTItemResourceType, access: HTStorageAccess): Boolean = super.isStackValidForInsert(resource, access) && this.canInsert.test(resource, access)

    override fun canStackExtract(resource: HTItemResourceType, access: HTStorageAccess): Boolean = super.canStackExtract(resource, access) && this.canExtract.test(resource, access)

    override fun getCapacity(resource: HTItemResourceType?): Int = if (resource == null) limit else minOf(limit, resource.toStack().maxStackSize)
}
