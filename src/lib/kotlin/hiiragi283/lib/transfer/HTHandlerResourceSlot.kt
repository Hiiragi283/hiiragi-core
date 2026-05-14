package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

@JvmRecord
data class HTHandlerResourceSlot<T : Resource>(private val handler: ResourceHandler<T>, private val index: Int) : HTResourceSlot<T> {
    override fun isValid(resource: T): Boolean = handler.isValid(index, resource)

    override fun insert(resource: T, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int = when (handler) {
        is HTResourceHandler<T> -> handler.insert(index, resource, amount, transaction, access)
        else -> handler.insert(index, resource, amount, transaction)
    }

    override fun extract(resource: T, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int = when (handler) {
        is HTResourceHandler<T> -> handler.extract(index, resource, amount, transaction, access)
        else -> handler.extract(index, resource, amount, transaction)
    }

    override fun getResource(): T = handler.getResource(index)

    override fun getAmountAsLong(): Long = handler.getAmountAsLong(index)

    override fun getAmountAsInt(): Int = handler.getAmountAsInt(index)

    override fun getCapacityAsLong(resource: T): Long = handler.getCapacityAsLong(index, resource)

    override fun getCapacityAsInt(resource: T): Int = handler.getCapacityAsInt(index, resource)
}
