package hiiragi283.core.api.storage.item

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.resource.HTResourceView
import net.minecraft.world.item.ItemStack

typealias HTItemViewNew = HTResourceView<HTItemResourceType>

fun HTItemViewNew.getItemStack(): ItemStack = this.getResource()?.toStack(this.getAmount()) ?: ItemStack.EMPTY

fun HTItemSlot.insert(stack: ItemStack, action: HTStorageAction, access: HTStorageAccess): ItemStack {
    val remainder: Int = this.insert(stack.toResource(), stack.count, action, access)
    return stack.copyWithCount(remainder)
}

fun HTItemSlot.extractItem(amount: Int, action: HTStorageAction, access: HTStorageAccess): ItemStack {
    val resourceIn: HTItemResourceType = this.getResource() ?: return ItemStack.EMPTY
    return this.extract(amount, action, access).let(resourceIn::toStack)
}
