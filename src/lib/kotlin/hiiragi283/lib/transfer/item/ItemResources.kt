package hiiragi283.lib.transfer.item

import hiiragi283.lib.transfer.HTResourceHandler
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.resource.ResourceStack

//    ItemResource    //

fun ItemStack.toResourcePair(): Pair<ItemResource, Int> = ItemResource.of(this) to this.count

fun ItemStackTemplate.toResourcePair(): Pair<ItemResource, Int> = ItemResource.of(this) to this.count

typealias ItemResourceStack = ResourceStack<ItemResource>

fun ItemResourceStack.toStack(): ItemStack = when {
    this.isEmpty -> ItemStack.EMPTY
    else -> this.resource().toStack(this.amount())
}

fun ItemStack.toResourceStack(): ItemResourceStack = ResourceStack(ItemResource.of(this), this.count)

//    ResourceHandler    //

typealias ItemResourceHandler = ResourceHandler<ItemResource>

typealias HTItemResourceHandler = HTResourceHandler<ItemResource>

fun ItemResourceHandler.getItemStack(index: Int): ItemStack = this.getResource(index).toStack(this.getAmountAsInt(index))
