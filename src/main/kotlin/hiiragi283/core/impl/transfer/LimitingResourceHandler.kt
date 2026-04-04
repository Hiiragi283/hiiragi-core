package hiiragi283.core.impl.transfer

import hiiragi283.core.api.transfer.indices
import net.neoforged.neoforge.transfer.DelegatingResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.function.Supplier

/**
 * @see net.neoforged.neoforge.transfer.energy.LimitingEnergyHandler
 */
class LimitingResourceHandler<T : Resource> : DelegatingResourceHandler<T> {
    private val inputSlots: IntArray
    private val outputSlots: IntArray

    constructor(delegate: ResourceHandler<T>, inputSlots: IntArray, outputSlots: IntArray) : super(delegate) {
        this.inputSlots = inputSlots
        this.outputSlots = outputSlots
    }

    constructor(delegate: Supplier<ResourceHandler<T>>, inputSlots: IntArray, outputSlots: IntArray) : super(delegate) {
        this.inputSlots = inputSlots
        this.outputSlots = outputSlots
    }

    override fun insert(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = when {
        index in inputSlots -> super.insert(index, resource, amount, transaction)
        else -> 0
    }

    override fun insert(resource: T, amount: Int, transaction: TransactionContext): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        var inserted = 0
        for (index: Int in this.indices) {
            inserted += insert(index, resource, amount - inserted, transaction)
            if (inserted == amount) break
        }
        return inserted
    }

    override fun extract(
        index: Int,
        resource: T,
        amount: Int,
        transaction: TransactionContext,
    ): Int = when {
        index in outputSlots -> super.extract(index, resource, amount, transaction)
        else -> 0
    }

    override fun extract(resource: T, amount: Int, transaction: TransactionContext): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        var extracted = 0
        for (index: Int in this.indices) {
            extracted += extract(index, resource, amount - extracted, transaction)
            if (extracted == amount) break
        }
        return extracted
    }
}
