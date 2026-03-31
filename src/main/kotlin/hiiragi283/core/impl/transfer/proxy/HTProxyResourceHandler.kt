package hiiragi283.core.impl.transfer.proxy

import hiiragi283.core.api.transfer.SidedResourceHandler
import hiiragi283.core.api.transfer.holder.HTCapabilityHolder
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTProxyResourceHandler<T : Resource>(private val handler: SidedResourceHandler<T>, side: Direction?, holder: HTCapabilityHolder?) :
    HTProxyHandler(side, holder),
    ResourceHandler<T> {
    override fun size(): Int = handler.size(side)

    override fun getResource(index: Int): T = handler.getResource(index, side)

    override fun getAmountAsLong(index: Int): Long = handler.getAmountAsLong(index, side)

    override fun getCapacityAsLong(index: Int, resource: T): Long = handler.getCapacityAsLong(index, resource, side)

    override fun isValid(index: Int, resource: T): Boolean = !readOnly || handler.isValid(index, resource, side)

    override fun insert(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = when {
        readOnlyInsert -> 0
        else -> handler.insert(index, resource, amount, transaction, side)
    }

    override fun extract(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = when {
        readOnlyExtract -> 0
        else -> handler.extract(index, resource, amount, transaction, side)
    }
}
