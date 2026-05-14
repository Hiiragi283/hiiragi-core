package hiiragi283.lib.transfer.item

import hiiragi283.lib.transfer.HTResourceHandler
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource

typealias ItemResourceHandler = ResourceHandler<ItemResource>

typealias HTItemResourceHandler = HTResourceHandler<ItemResource>

fun ItemResourceHandler.getItemStack(index: Int): ItemStack = this.getResource(index).toStack(this.getAmountAsInt(index))

fun ItemStack.toResourcePair(): Pair<ItemResource, Int> = ItemResource.of(this) to this.count

fun ItemStackTemplate.toResourcePair(): Pair<ItemResource, Int> = ItemResource.of(this) to this.count
