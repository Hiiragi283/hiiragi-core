package hiiragi283.core.impl.storage.item

import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.storage.item.toStackOrEmpty
import hiiragi283.core.impl.storage.resource.HTStackResourceSlot
import net.minecraft.world.item.ItemStack

abstract class HTItemStackResourceSlot : HTStackResourceSlot<ItemStack, HTItemResourceType>() {
    final override fun getResourceFrom(stack: ItemStack): HTItemResourceType? = stack.toResource()

    final override fun getAmountFrom(stack: ItemStack): Int = stack.count

    final override fun isSame(stack: ItemStack, resource: HTItemResourceType): Boolean = stack.toResource() == resource

    final override fun createStack(resource: HTItemResourceType?, amount: Int): ItemStack = resource.toStackOrEmpty(amount)
}
