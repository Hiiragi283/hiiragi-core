package hiiragi283.core.api.transfer

import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface StrictResourceSlot<T : Resource> : ResourceSlot<T> {
    companion object {
        @JvmStatic
        fun <T : Resource> of(handler: StrictResourceHandler<T>, index: Int): StrictResourceSlot<T> =
            object : StrictResourceSlot<T>, ResourceView<T> by ResourceView.of(handler, index) {
                override fun insert(
                    resource: T,
                    amount: Int,
                    transaction: TransactionContext,
                    access: HTHandlerAccess,
                ): Int = handler.insert(index, resource, amount, transaction, access)

                override fun extract(
                    resource: T,
                    amount: Int,
                    transaction: TransactionContext,
                    access: HTHandlerAccess,
                ): Int = handler.extract(index, resource, amount, transaction, access)
            }
    }

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

    @Deprecated("Use 'insert(T, Int, TransactionContext, HTHandlerAccess)' instead")
    override fun insert(resource: T, amount: Int, transaction: TransactionContext): Int =
        insert(resource, amount, transaction, HTHandlerAccess.EXTERNAL)

    @Deprecated("Use 'extract(T, Int, TransactionContext, HTHandlerAccess)' instead")
    override fun extract(resource: T, amount: Int, transaction: TransactionContext): Int =
        extract(resource, amount, transaction, HTHandlerAccess.EXTERNAL)
}
