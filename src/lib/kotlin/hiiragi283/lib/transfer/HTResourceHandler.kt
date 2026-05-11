package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

@Suppress("NonExtendableApiUsage")
fun interface HTResourceHandler<T : Resource> : ResourceHandler<T> {
    fun getSlots(): List<HTResourceSlot<T>>

    fun getSlot(index: Int): HTResourceSlot<T> = getSlots()[index]

    override fun size(): Int = getSlots().size

    override fun getResource(index: Int): T = getSlot(index).getResource()

    override fun getAmountAsLong(index: Int): Long = getSlot(index).getAmountAsLong()

    override fun getAmountAsInt(index: Int): Int = getSlot(index).getAmountAsInt()

    override fun getCapacityAsLong(index: Int, resource: T): Long = getSlot(index).getCapacityAsLong(resource)

    override fun getCapacityAsInt(index: Int, resource: T): Int = getSlot(index).getCapacityAsInt(resource)

    override fun isValid(index: Int, resource: T): Boolean = getSlot(index).isValid(resource)

    override fun insert(index: Int, resource: T, amount: Int, transaction: TransactionContext): Int = getSlot(index).insert(resource, amount, HTHandlerAccess.EXTERNAL, transaction)

    override fun extract(index: Int, resource: T, amount: Int, transaction: TransactionContext): Int = getSlot(index).extract(resource, amount, HTHandlerAccess.EXTERNAL, transaction)
}
