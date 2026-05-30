package hiiragi283.lib.transfer.item

import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.HTResourceView
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource

//    ItemResource    //

fun ItemStack.toResourcePair(): Pair<ItemResource, Int> = ItemResource.of(this) to this.count

fun ItemStackTemplate.toResourcePair(): Pair<ItemResource, Int> = ItemResource.of(this) to this.count

//    ResourceHandler    //

typealias ItemResourceHandler = ResourceHandler<ItemResource>

fun ItemResourceHandler.getItemStack(index: Int): ItemStack = this.getResource(index).toStack(this.getAmountAsInt(index))

typealias HTItemView = HTResourceView<ItemResource>

fun HTItemView.getItemStack(): ItemStack = this.resource.toStack(this.amountAsInt)

typealias HTItemSlot = HTResourceSlot<ItemResource>
