package hiiragi283.core.api.transfer.item

import hiiragi283.core.api.transfer.ResourceHandlerDefinition
import hiiragi283.core.api.transfer.StrictStacksResourceHandler
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * @see net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler
 */
class StrictItemStacksResourceHandler : StrictStacksResourceHandler<ItemStack, ItemResource> {
    constructor(definition: ResourceHandlerDefinition<ItemResource>) : super(definition, ItemStack.EMPTY, ItemStack.OPTIONAL_CODEC)

    constructor(stacks: NonNullList<ItemStack>, definition: ResourceHandlerDefinition<ItemResource>) : super(
        stacks,
        definition,
        ItemStack.EMPTY,
        ItemStack.OPTIONAL_CODEC,
    )

    override fun getResourceFrom(stack: ItemStack): ItemResource = ItemResource.of(stack)

    override fun getAmountFrom(stack: ItemStack): Int = stack.count

    override fun getStackFrom(resource: ItemResource, amount: Int): ItemStack = resource.toStack(amount)

    override fun copyOf(stack: ItemStack): ItemStack = stack.copy()

    override fun getCapacity(index: Int, resource: ItemResource): Int = when {
        resource.isEmpty -> super.getCapacity(index, resource)
        else -> minOf(resource.maxStackSize, super.getCapacity(index, resource))
    }

    override fun matches(stack: ItemStack, resource: ItemResource): Boolean = resource.matches(stack)
}
