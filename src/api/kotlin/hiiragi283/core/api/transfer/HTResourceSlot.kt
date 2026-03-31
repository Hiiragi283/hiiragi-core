package hiiragi283.core.api.transfer

import hiiragi283.core.api.HTContentListener
import net.neoforged.neoforge.common.util.ValueIOSerializable
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface HTResourceSlot<T : Resource> :
    HTResourceView<T>,
    HTContentListener,
    ValueIOSerializable {
    fun isValid(resource: T): Boolean

    fun insert(
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int

    fun extract(
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int
}
