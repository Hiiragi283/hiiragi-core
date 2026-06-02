package hiiragi283.lib.transfer

import com.google.common.primitives.Ints
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.toOption
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
    protected val emptyResource: RESOURCE,
) : SnapshotJournal<Option<HTResourceStack<RESOURCE>>>(),
    HTResourceSlot<RESOURCE> {
    @JvmField
    protected var stackIn: HTResourceStack<RESOURCE>? = null

    fun setContents(resource: RESOURCE, amount: Long, transaction: TransactionContext?) {
        setContents(HTResourceStack.of(resource, amount), transaction)
    }

    fun setContents(stack: HTResourceStack<RESOURCE>?, transaction: TransactionContext?) {
        if (stack != this.stackIn) {
            val option: Option<HTResourceStack<RESOURCE>> = stack.toOption()
            if (transaction == null) {
                val original: Option<HTResourceStack<RESOURCE>> = createSnapshot()
                revertToSnapshot(option)
                onRootCommit(original)
            } else {
                updateSnapshots(transaction)
                revertToSnapshot(option)
            }
        }
    }

    fun isValidForInsertion(resource: RESOURCE, access: HTHandlerAccess): Boolean = this.canInsert.test(resource, access)

    fun canResourceExtract(resource: RESOURCE, access: HTHandlerAccess): Boolean = this.canExtract.test(resource, access)

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
            needed <= 0 || !isValidForInsertion(resource, access) -> 0
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
        if (amount == 0 || resource != this.resource || !canResourceExtract(resource, access)) return 0
        val currentAmount: Long = this.amountAsLong
        var toRemove: Int = minOf(amount, this.amountAsInt)
        toRemove = minOf(toRemove, getExtractionRate(access).let(Ints::saturatedCast))
        if (toRemove > 0) {
            setContents(resource, currentAmount - toRemove, transaction)
        }
        return toRemove
    }

    final override val resource: RESOURCE get() = stackIn?.resource ?: emptyResource
    final override val amountAsLong: Long get() = stackIn?.amountAsLong ?: 0
    override val amountAsInt: Int get() = stackIn?.amountAsInt ?: 0

    override fun getCapacityAsLong(resource: RESOURCE): Long = capacity

    //    SnapshotJournal    //

    override fun createSnapshot(): Option<HTResourceStack<RESOURCE>> = stackIn.toOption()

    override fun revertToSnapshot(snapshot: Option<HTResourceStack<RESOURCE>>) {
        this.stackIn = snapshot.getOrNull()
    }

    override fun onRootCommit(originalState: Option<HTResourceStack<RESOURCE>>) {
        listener?.run()
    }
}
