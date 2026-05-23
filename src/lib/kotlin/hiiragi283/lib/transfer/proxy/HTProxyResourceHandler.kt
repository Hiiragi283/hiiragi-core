package hiiragi283.lib.transfer.proxy

import hiiragi283.lib.transfer.HTResourceHandler
import hiiragi283.lib.transfer.holder.HTCapabilityHolder
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTProxyResourceHandler<RESOURCE : Resource>(private val handler: HTResourceHandler<RESOURCE, *>, side: Direction?, holder: HTCapabilityHolder?) :
    HTProxyHandler(side, holder),
    ResourceHandler<RESOURCE> {
    override fun size(): Int = handler.size()

    override fun getResource(index: Int): RESOURCE = handler.getResource(index)

    override fun getAmountAsLong(index: Int): Long = handler.getAmountAsLong(index)

    override fun getCapacityAsLong(index: Int, resource: RESOURCE): Long = handler.getCapacityAsLong(index, resource)

    override fun isValid(index: Int, resource: RESOURCE): Boolean = handler.isValid(index, resource)

    override fun insert(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = when {
        readOnlyInsert -> 0
        else -> handler.insert(index, resource, amount, transaction)
    }

    override fun insert(resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = when {
        readOnlyInsert -> 0
        else -> handler.insert(resource, amount, transaction)
    }

    override fun extract(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = when {
        readOnlyExtract -> 0
        else -> handler.extract(index, resource, amount, transaction)
    }

    override fun extract(resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = when {
        readOnlyExtract -> 0
        else -> handler.extract(resource, amount, transaction)
    }
}
