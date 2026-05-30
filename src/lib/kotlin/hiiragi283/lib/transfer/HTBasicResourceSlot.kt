package hiiragi283.lib.transfer

import com.google.common.primitives.Ints
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext

abstract class HTBasicResourceSlot<RESOURCE : Resource>(
    protected val capacity: Long,
    private val canInsert: BiPredicate<RESOURCE, HTHandlerAccess>,
    private val canExtract: BiPredicate<RESOURCE, HTHandlerAccess>,
    private val filter: Predicate<RESOURCE>,
    private val listener: Runnable?,
    private val emptyResource: RESOURCE,
) : SnapshotJournal<Pair<RESOURCE, Long>>(),
    HTResourceSlot<RESOURCE> {
    @JvmField
    protected var resourceIn: RESOURCE = emptyResource

    @JvmField
    protected var amountIn: Long = 0

    fun setContents(resource: RESOURCE, amount: Long, transaction: TransactionContext?) {
        if (resource != this.resourceIn || amount != this.amountIn) {
            val fixedResource: RESOURCE = when {
                amount <= 0 -> emptyResource
                else -> resource
            }
            if (transaction == null) {
                val original: Pair<RESOURCE, Long> = createSnapshot()
                revertToSnapshot(fixedResource to amount)
                onRootCommit(original)
            } else {
                updateSnapshots(transaction)
                revertToSnapshot(fixedResource to amount)
            }
        }
    }

    protected open fun getInsertionRate(access: HTHandlerAccess): Long = Long.MAX_VALUE

    protected open fun getExtractionRate(access: HTHandlerAccess): Long = Long.MAX_VALUE

    //    HTResourceSlot    //

    final override fun isValid(resource: RESOURCE): Boolean = this.filter.test(resource)

    override fun insert(resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        if (amount == 0) return 0
        var needed: Long = getNeededAsLong(resource)
        needed = minOf(needed, getInsertionRate(access))
        return when {
            // 空きがない，または搬入条件を満たしていない場合
            needed <= 0 || !this.canInsert.test(resource, access) -> 0
            // 中身が存在し，resourceと異なる場合
            !isEmpty() && resource != this.resource -> 0
            else -> {
                val toAdd: Int = minOf(amount, Ints.saturatedCast(needed))
                setContents(resource, this.amountAsLong + toAdd, transaction)
                toAdd
            }
        }
    }

    override fun extract(resource: RESOURCE, amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        if (amount == 0 || resource != this.resource || !this.canExtract.test(resource, access)) return 0
        val currentAmount: Long = this.amountAsLong
        var toRemove: Int = minOf(amount, this.amountAsInt)
        toRemove = minOf(toRemove, getExtractionRate(access).let(Ints::saturatedCast))
        if (toRemove > 0) {
            setContents(resource, currentAmount - toRemove, transaction)
        }
        return toRemove
    }

    final override val resource: RESOURCE get() = resourceIn
    final override val amountAsLong: Long get() = amountIn

    override fun getCapacityAsLong(resource: RESOURCE): Long = capacity

    //    SnapshotJournal    //

    override fun createSnapshot(): Pair<RESOURCE, Long> = resource to amountIn

    override fun revertToSnapshot(snapshot: Pair<RESOURCE, Long>) {
        val (resource: RESOURCE, amount: Long) = snapshot
        resourceIn = resource
        amountIn = amount
    }

    override fun onRootCommit(originalState: Pair<RESOURCE, Long>) {
        listener?.run()
    }
}
