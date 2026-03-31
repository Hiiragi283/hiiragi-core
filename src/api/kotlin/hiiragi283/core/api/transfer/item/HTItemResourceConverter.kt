package hiiragi283.core.api.transfer.item

import hiiragi283.core.api.transfer.HTResourceConverter
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource

object HTItemResourceConverter : HTResourceConverter<ItemStack, ItemResource> {
    override fun getEmptyStack(): ItemStack = ItemStack.EMPTY

    override fun getResource(stack: ItemStack): ItemResource = ItemResource.of(stack)

    override fun getAmount(stack: ItemStack): Long = stack.count.toLong()

    override fun setAmount(stack: ItemStack, amount: Long) {
        stack.count = amount.toInt()
    }

    override fun copyStack(stack: ItemStack): ItemStack = stack.copy()

    override fun createStack(resource: ItemResource, amount: Int): ItemStack = resource.toStack(amount)
}
