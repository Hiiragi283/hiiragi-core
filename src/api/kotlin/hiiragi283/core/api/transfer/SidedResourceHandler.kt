package hiiragi283.core.api.transfer

import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface SidedResourceHandler<T : Resource> : ResourceHandler<T> {
    fun getHandlerSideFor(): Direction? = null

    fun size(side: Direction?): Int

    @Deprecated("Use 'size(Direction?)' instead")
    override fun size(): Int = size(getHandlerSideFor())

    fun getResource(index: Int, side: Direction?): T

    @Deprecated("Use 'getResource(Int, Direction?)' instead")
    override fun getResource(index: Int): T = getResource(index, getHandlerSideFor())

    fun getAmountAsLong(index: Int, side: Direction?): Long

    @Deprecated("Use 'getAmountAsLong(Int, Direction?)' instead")
    override fun getAmountAsLong(index: Int): Long = getAmountAsLong(index, getHandlerSideFor())

    fun getCapacityAsLong(index: Int, resource: T, side: Direction?): Long

    @Deprecated("Use 'getCapacityAsLong(Int, T, Direction?)' instead")
    override fun getCapacityAsLong(index: Int, resource: T): Long = getCapacityAsLong(index, resource, getHandlerSideFor())

    fun isValid(index: Int, resource: T, side: Direction?): Boolean

    @Deprecated("Use 'isValid(Int, T, Direction?)' instead")
    override fun isValid(index: Int, resource: T): Boolean = isValid(index, resource, getHandlerSideFor())

    fun insert(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        side: Direction?,
    ): Int

    @Deprecated("Use 'insert(Int, T, Int, TransactionContext, Direction?)' instead")
    override fun insert(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = insert(index, resource, amount, transaction, getHandlerSideFor())

    fun extract(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        side: Direction?,
    ): Int

    @Deprecated("Use 'extract(Int, T, Int, TransactionContext, Direction?)' instead")
    override fun extract(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = extract(index, resource, amount, transaction, getHandlerSideFor())
}
