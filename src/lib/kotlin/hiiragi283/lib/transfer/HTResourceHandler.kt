package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTResourceHandler<T : Resource> : ResourceHandler<T> {
    fun insert(index: Int, resource: T, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int

    fun extract(index: Int, resource: T, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int

    override fun insert(index: Int, resource: T, amount: Int, transaction: TransactionContext): Int = insert(index, resource, amount, transaction, HTHandlerAccess.EXTERNAL)

    override fun extract(index: Int, resource: T, amount: Int, transaction: TransactionContext): Int = extract(index, resource, amount, transaction, HTHandlerAccess.EXTERNAL)
}
