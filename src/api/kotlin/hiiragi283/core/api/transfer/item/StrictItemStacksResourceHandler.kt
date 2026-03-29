package hiiragi283.core.api.transfer.item

import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.StrictResourceHandler
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

abstract class StrictItemStacksResourceHandler :
    ItemStacksResourceHandler,
    StrictResourceHandler<ItemResource> {
    constructor(size: Int) : super(size)

    constructor(stacks: NonNullList<ItemStack>) : super(stacks)

    protected abstract fun canInsert(index: Int, access: HTHandlerAccess): Boolean

    protected abstract fun canExtract(index: Int, access: HTHandlerAccess): Boolean

    override fun insert(
        index: Int,
        resource: ItemResource,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int = when {
        canInsert(index, access) -> super<ItemStacksResourceHandler>.insert(index, resource, amount, transaction)
        else -> 0
    }

    override fun extract(
        index: Int,
        resource: ItemResource,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int = when {
        canExtract(index, access) -> super<ItemStacksResourceHandler>.extract(index, resource, amount, transaction)
        else -> 0
    }

    @Suppress("DEPRECATION")
    final override fun insert(
        index: Int,
        resource: ItemResource,
        amount: Int,
        transaction: TransactionContext,
    ): Int = super<StrictResourceHandler>.insert(index, resource, amount, transaction)

    @Suppress("DEPRECATION")
    final override fun extract(
        index: Int,
        resource: ItemResource,
        amount: Int,
        transaction: TransactionContext,
    ): Int = super<StrictResourceHandler>.extract(index, resource, amount, transaction)
}
