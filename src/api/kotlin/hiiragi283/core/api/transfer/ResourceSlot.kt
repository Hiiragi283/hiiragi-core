package hiiragi283.core.api.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

interface ResourceSlot<T : Resource> : ResourceView<T> {
    companion object {
        @JvmStatic
        fun <T : Resource> of(handler: ResourceHandler<T>, index: Int): ResourceSlot<T> =
            object : ResourceSlot<T>, ResourceView<T> by ResourceView.of(handler, index) {
                override fun insert(resource: T, amount: Int, transaction: TransactionContext): Int =
                    handler.insert(index, resource, amount, transaction)

                override fun extract(resource: T, amount: Int, transaction: TransactionContext): Int =
                    handler.extract(index, resource, amount, transaction)
            }
    }

    fun insert(resource: T, amount: Int, transaction: TransactionContext): Int

    fun extract(resource: T, amount: Int, transaction: TransactionContext): Int
}
