package hiiragi283.core.impl.transfer

import com.mojang.serialization.Codec
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.HTResourceConverter
import hiiragi283.core.api.transfer.HTResourceSlot
import hiiragi283.core.api.transfer.HTSlotModifier
import hiiragi283.core.api.transfer.getNeededAsInt
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.resource.Resource
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see net.neoforged.neoforge.transfer.StacksResourceHandler
 */
abstract class HTBasicResourceSlot<T : Resource> :
    HTResourceSlot<T>,
    HTSlotModifier<T> {
    override fun insert(
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        val needed: Int = minOf(inputRate(access), getNeededAsInt(resource))
        if (needed <= 0 || !isStackValidForInsert(resource, access)) return 0

        val resourceIn: T = this.resource
        val isSameType: Boolean = resourceIn == resource
        if (resourceIn.isEmpty || isSameType) {
            val inserted: Int = minOf(amount, needed)
            if (inserted > 0) {
                updateSnapshot(transaction)
                if (isSameType) {
                    this.amountAsLong += inserted
                } else {
                    updateStack(resource, inserted)
                }
                return inserted
            }
        }
        return 0
    }

    override fun extract(
        resource: T,
        amount: Int,
        transaction: TransactionContext,
        access: HTHandlerAccess,
    ): Int {
        TransferPreconditions.checkNonEmptyNonNegative(resource, amount)
        if (!canStackExtract(resource, access) || resource != this.resource) return 0
        val extracted: Int = minOf(minOf(outputRate(access), this.amountAsInt), amount)
        if (extracted > 0) {
            updateSnapshot(transaction)
            this.amountAsLong -= extracted
            return extracted
        }
        return 0
    }

    protected abstract fun updateStack(resource: T, amount: Int)

    protected abstract fun updateSnapshot(transaction: TransactionContext)

    override fun set(resource: T, amount: Int) {
        updateStack(resource, amount)
    }

    abstract override var amountAsLong: Long
    final override var amountAsInt: Int
        get() = super.amountAsInt
        set(value) {
            amountAsLong = value.toLong()
        }

    /**
     * 指定したリソースをこのスロットに搬入できるか判定します。
     * @param resource 搬入されるリソース
     * @param access このスロットへのアクセスの種類
     * @return 搬入できる場合は`true`
     */
    open fun isStackValidForInsert(resource: T, access: HTHandlerAccess): Boolean = isValid(resource)

    /**
     * 指定したリソースをこのスロットから搬出できるか判定します。
     * @param resource 搬出されるリソース
     * @param access このスロットへのアクセスの種類
     * @return 搬出できる場合は`true`
     */
    open fun canStackExtract(resource: T, access: HTHandlerAccess): Boolean = true

    /**
     * 一度に搬入される量の上限を返します。
     * @param access このスロットへのアクセスの種類
     */
    protected open fun inputRate(access: HTHandlerAccess): Int = Int.MAX_VALUE

    /**
     * 一度に搬出される量の上限を返します。
     * @param access このスロットへのアクセスの種類
     */
    protected open fun outputRate(access: HTHandlerAccess): Int = Int.MAX_VALUE

    //    Stacked    //

    abstract class Stacked<S : Any, T : Resource>(
        private val key: String,
        private val codec: Codec<S>,
        private val canExtract: BiPredicate<T, HTHandlerAccess>,
        private val canInsert: BiPredicate<T, HTHandlerAccess>,
        private val filter: Predicate<T>,
        private val listener: HTContentListener?,
    ) : HTBasicResourceSlot<T>() {
        @JvmField
        protected var stack: S = getConverter().getEmptyStack()
        private val journal = StackJournal()

        protected abstract fun getConverter(): HTResourceConverter<S, T>

        protected fun updateStack(stack: S, validate: Boolean) {
            val resource: T = getConverter().getResource(stack)
            if (resource.isEmpty) {
                if (this.resource.isEmpty) return
                this.stack = getConverter().getEmptyStack()
            } else if (!validate || isValid(resource)) {
                this.stack = stack
            } else {
                error("Invalid stack for slot: $stack")
            }
            onContentsChanged()
        }

        //    HTBasicResourceSlot    //

        override fun updateSnapshot(transaction: TransactionContext) {
            journal.updateSnapshots(transaction)
        }

        override val resource: T get() = getConverter().getResource(stack)
        override var amountAsLong: Long
            get() = getConverter().getAmount(stack)
            set(value) {
                getConverter().setAmount(stack, value)
            }

        override fun updateStack(resource: T, amount: Int) {
            updateStack(getConverter().createStack(resource, amount), false)
        }

        final override fun isValid(resource: T): Boolean = filter.test(resource)

        final override fun isStackValidForInsert(resource: T, access: HTHandlerAccess): Boolean =
            super.isStackValidForInsert(resource, access) && canInsert.test(resource, access)

        final override fun canStackExtract(resource: T, access: HTHandlerAccess): Boolean =
            super.canStackExtract(resource, access) && canExtract.test(resource, access)

        final override fun onContentsChanged() {
            listener?.onContentsChanged()
        }

        override fun serialize(output: ValueOutput) {
            if (!resource.isEmpty) {
                output.store(key, codec, stack)
            }
        }

        override fun deserialize(input: ValueInput) {
            input.read(key, codec).ifPresent { stack: S -> updateStack(stack, true) }
        }

        private inner class StackJournal : SnapshotJournal<S>() {
            override fun createSnapshot(): S = this@Stacked.getConverter().copyStack(this@Stacked.stack)

            override fun revertToSnapshot(shapshot: S) {
                this@Stacked.stack = shapshot
            }

            override fun onRootCommit(originalState: S) {
                this@Stacked.onContentsChanged()
            }
        }
    }
}
