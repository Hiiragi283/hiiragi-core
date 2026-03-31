package hiiragi283.core.api.transfer

import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

fun interface HTResourceHandler<T : Resource> : SidedResourceHandler<T> {
    fun getSlots(side: Direction?): List<HTResourceSlot<T>>

    fun getSlot(index: Int, side: Direction?): HTResourceSlot<T> = getSlots(side)[index]

    fun hasResourceHandler(): Boolean = false

    override fun size(side: Direction?): Int = getSlots(side).size

    override fun getResource(index: Int, side: Direction?): T = getSlot(index, side).resource

    override fun getAmountAsLong(index: Int, side: Direction?): Long = getSlot(index, side).amountAsLong

    override fun getCapacityAsLong(index: Int, resource: T, side: Direction?): Long = getSlot(index, side).getCapacityAsLong(resource)

    override fun isValid(index: Int, resource: T, side: Direction?): Boolean = getSlot(index, side).isValid(resource)

    override fun insert(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        side: Direction?,
    ): Int = getSlot(index, side).insert(resource, amount, transaction, HTHandlerAccess.forHandler(side))

    override fun extract(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        side: Direction?,
    ): Int = getSlot(index, side).extract(resource, amount, transaction, HTHandlerAccess.forHandler(side))
}
