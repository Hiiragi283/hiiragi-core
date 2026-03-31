package hiiragi283.core.impl.transfer.energy

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.transfer.HTHandlerAccess
import hiiragi283.core.api.transfer.HTTransferPredicates
import hiiragi283.core.api.transfer.energy.HTEnergyBattery
import hiiragi283.core.api.transfer.energy.neededAsInt
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.TransferPreconditions
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.function.Predicate

open class HTBasicEnergyBattery(
    private val capacity: Int,
    protected val canExtract: Predicate<HTHandlerAccess>,
    protected val canInsert: Predicate<HTHandlerAccess>,
    private val listener: HTContentListener?,
) : HTEnergyBattery {
    companion object {
        @JvmStatic
        private fun validateCapacity(capacity: Int): Int {
            check(capacity >= 0) { "Capacity must be non negative" }
            return capacity
        }

        @JvmStatic
        fun input(listener: HTContentListener?, capacity: Int): HTBasicEnergyBattery =
            create(listener, capacity, HTHandlerAccess.NOT_EXTERNAL, HTTransferPredicates.alwaysTrue())

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Int): HTBasicEnergyBattery =
            create(listener, capacity, HTTransferPredicates.alwaysTrue(), HTHandlerAccess.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: Int,
            canExtract: Predicate<HTHandlerAccess> = HTTransferPredicates.alwaysTrue(),
            canInsert: Predicate<HTHandlerAccess> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicEnergyBattery = HTBasicEnergyBattery(validateCapacity(capacity), canExtract, canInsert, listener)
    }

    @JvmField
    protected var amount: Int = 0

    private val journal = AmountJournal()

    fun setAmountUnchecked(amount: Int, validate: Boolean = false) {
        if (amount == 0) {
            if (this.amount == 0) return
            this.amount = 0
        } else if (!validate || amount > 0) {
            this.amount = amount.coerceIn(0, capacityAsInt)
        } else {
            error("Invalid amount for storage: $amount")
        }
        onContentsChanged()
    }

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

    //    HTEnergyBattery    //

    override fun insert(amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int {
        TransferPreconditions.checkNonNegative(amount)
        if (!this.canInsert.test(access)) return 0
        val needed: Int = minOf(inputRate(access), this.neededAsInt)
        if (needed <= 0) return 0
        val inserted: Int = minOf(amount, needed)
        if (inserted > 0) {
            journal.updateSnapshots(transaction)
            this.amount += inserted
            return inserted
        }
        return 0
    }

    override fun extract(amount: Int, transaction: TransactionContext, access: HTHandlerAccess): Int {
        TransferPreconditions.checkNonNegative(amount)
        if (!this.canExtract.test(access)) return 0
        val extracted: Int = minOf(minOf(outputRate(access), this.amountAsInt), amount)
        if (extracted > 0) {
            journal.updateSnapshots(transaction)
            this.amount -= extracted
            return extracted
        }
        return 0
    }

    override fun getAmountAsLong(): Long = amount.toLong()

    override fun getCapacityAsLong(): Long = capacity.toLong()

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun serialize(output: ValueOutput) {
        output.putInt(HTConst.AMOUNT, amount)
    }

    override fun deserialize(input: ValueInput) {
        input.getInt(HTConst.AMOUNT).ifPresent(::setAmountUnchecked)
    }

    private inner class AmountJournal : SnapshotJournal<Int>() {
        override fun createSnapshot(): Int = this@HTBasicEnergyBattery.amount

        override fun revertToSnapshot(snapshot: Int) {
            this@HTBasicEnergyBattery.amount = snapshot
        }

        override fun onRootCommit(originalState: Int) {
            this@HTBasicEnergyBattery.onContentsChanged()
        }
    }
}
