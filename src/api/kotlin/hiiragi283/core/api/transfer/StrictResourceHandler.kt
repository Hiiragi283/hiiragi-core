package hiiragi283.core.api.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface StrictResourceHandler<T : Resource> : ResourceHandler<T> {
    fun insert(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int

    fun extract(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int

    @Deprecated("Use `insert(Int, T, Int, TransactionContext, HTHandlerAccess)` instead")
    override fun insert(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = insert(index, resource, amount, transaction, HTHandlerAccess.EXTERNAL)

    @Deprecated("Use `extract(Int, T, Int, TransactionContext, HTHandlerAccess)` instead")
    override fun extract(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = extract(index, resource, amount, transaction, HTHandlerAccess.EXTERNAL)
}
