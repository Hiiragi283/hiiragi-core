package hiiragi283.core.api.transfer.item

import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.HTResourceSlot
import hiiragi283.core.api.transfer.HTResourceView
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

fun ItemStack.toResourcePair(): Pair<ItemResource, Int> = ItemResource.of(this) to this.count

/**
 * [ItemResource]向けの[HTResourceView]のエイリアス
 */
typealias HTItemView = HTResourceView<ItemResource>

/**
 * [ItemResource]向けの[HTResourceSlot]のエイリアス
 */
typealias HTItemSlot = HTResourceSlot<ItemResource>

val HTItemView.stack: ItemStack get() = this.resource.toStack(this.amountAsInt)

fun HTItemSlot.insert(stack: ItemStack, transaction: TransactionContext, access: HTHandlerAccess): Int {
    val (resource: ItemResource, count: Int) = stack.toResourcePair()
    return when {
        resource.isEmpty -> 0
        else -> this.insert(resource, count, transaction, access)
    }
}

fun HTItemSlot.extract(stack: ItemStack, transaction: TransactionContext, access: HTHandlerAccess): Int {
    val (resource: ItemResource, count: Int) = stack.toResourcePair()
    return when {
        resource.isEmpty -> 0
        else -> this.extract(resource, count, transaction, access)
    }
}
