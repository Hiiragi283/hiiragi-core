package hiiragi283.core.api.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

fun interface HTResourceHandler<T : Resource> : ResourceHandler<T> {
    fun getSlots(): List<HTResourceSlot<T>>

    fun getSlot(index: Int): HTResourceSlot<T> = getSlots()[index]

    override fun size(): Int = getSlots().size

    override fun getResource(index: Int): T = getSlot(index).resource

    override fun getAmountAsLong(index: Int): Long = getSlot(index).amountAsLong

    override fun getCapacityAsLong(index: Int, resource: T): Long = getSlot(index).getCapacityAsLong(resource)

    override fun isValid(index: Int, resource: T): Boolean = getSlot(index).isValid(resource)

    override fun insert(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = getSlot(index).insert(resource, amount, transaction, HTHandlerAccess.EXTERNAL)

    override fun extract(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = getSlot(index).extract(resource, amount, transaction, HTHandlerAccess.EXTERNAL)
}
