package hiiragi283.lib.transfer

import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

fun interface HTResourceHandler<RESOURCE : Resource, SLOT : HTResourceSlot<RESOURCE>> : ResourceHandler<RESOURCE> {
    fun getSlots(): List<SLOT>

    fun getSlot(index: Int): SLOT = getSlots()[index]

    override fun size(): Int = getSlots().size

    override fun getResource(index: Int): RESOURCE = getSlot(index).resource

    override fun getAmountAsLong(index: Int): Long = getSlot(index).amountAsLong

    @Suppress("NonExtendableApiUsage")
    override fun getAmountAsInt(index: Int): Int = getSlot(index).amountAsInt

    override fun getCapacityAsLong(index: Int, resource: RESOURCE): Long = getSlot(index).getCapacityAsLong(resource)

    @Suppress("NonExtendableApiUsage")
    override fun getCapacityAsInt(index: Int, resource: RESOURCE): Int = getSlot(index).getCapacityAsInt(resource)

    override fun isValid(index: Int, resource: RESOURCE): Boolean = getSlot(index).isValid(resource)

    override fun insert(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = this.insert(index, resource, amount, transaction, HTHandlerAccess.EXTERNAL)

    override fun extract(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext): Int = this.extract(index, resource, amount, transaction, HTHandlerAccess.EXTERNAL)

    fun insert(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int = getSlot(index).insert(resource, amount, transaction, access)

    fun insert(resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        var inserted = 0
        for (i: Int in this.indices) {
            inserted += insert(i, resource, amount - inserted, transaction, access)
            if (inserted == amount) break
        }
        return inserted
    }

    fun extract(index: Int, resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int = getSlot(index).extract(resource, amount, transaction, access)

    fun extract(resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        var extracted = 0
        for (i: Int in this.indices) {
            extracted += extract(i, resource, amount - extracted, transaction, access)
            if (extracted == amount) break
        }
        return extracted
    }
}
