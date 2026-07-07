package hiiragi283.lib.transfer

import java.util.function.Supplier
import net.neoforged.neoforge.transfer.DelegatingResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTStrictResourceHandler<RESOURCE : Resource> : DelegatingResourceHandler<RESOURCE> {
    private val filter: (Int) -> HTTransferIO

    constructor(delegate: Supplier<ResourceHandler<RESOURCE>>, filter: (Int) -> HTTransferIO) : super(delegate) {
        this.filter = filter
    }

    constructor(delegate: ResourceHandler<RESOURCE>, filter: (Int) -> HTTransferIO) : super(delegate) {
        this.filter = filter
    }

    override fun insert(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = when {
        filter(index).canInsert -> super.insert(index, resource, amount, transaction)
        else -> 0
    }

    override fun extract(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = when {
        filter(index).canExtract -> super.extract(index, resource, amount, transaction)
        else -> 0
    }
}
