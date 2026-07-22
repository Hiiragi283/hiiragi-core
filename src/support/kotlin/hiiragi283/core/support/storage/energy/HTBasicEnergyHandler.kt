package hiiragi283.core.support.storage.energy

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.amount.HTAmountSlot
import hiiragi283.core.api.storage.energy.HTEnergyHandler
import hiiragi283.core.support.storage.HTStorageValidators
import java.util.function.Predicate

open class HTBasicEnergyHandler(
    private val capacity: Int,
    protected val canExtract: Predicate<HTStorageAccess>,
    protected val canInsert: Predicate<HTStorageAccess>,
    private val listener: HTContentListener?,
) : HTAmountSlot.Basic(),
    HTEnergyHandler,
    HTContentListener {
    companion object {
        @JvmStatic
        fun input(listener: HTContentListener?, capacity: Int): HTBasicEnergyHandler = create(listener, capacity, HTStorageAccess.NOT_EXTERNAL, HTStoragePredicates.alwaysTrue())

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Int): HTBasicEnergyHandler = create(listener, capacity, HTStoragePredicates.alwaysTrue(), HTStorageAccess.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: Int,
            canExtract: Predicate<HTStorageAccess> = HTStoragePredicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = HTStoragePredicates.alwaysTrue(),
        ): HTBasicEnergyHandler = HTBasicEnergyHandler(HTStorageValidators.validateCapacity(capacity), canExtract, canInsert, listener)
    }

    @JvmField
    protected var amount: Int = 0

    override fun setAmount(amount: Int) {
        setAmountUnchecked(amount, true)
    }

    protected fun setAmountUnchecked(amount: Int, validate: Boolean = false) {
        if (amount == 0) {
            if (this.amount == 0) return
            this.amount = 0
        } else if (!validate || amount > 0) {
            this.amount = amount.coerceIn(0, getCapacity())
        } else {
            error("Invalid amount for storage: $amount")
        }
        onContentsChanged()
    }

    override fun canInsert(access: HTStorageAccess): Boolean = this.canInsert.test(access)

    override fun canExtract(access: HTStorageAccess): Boolean = this.canExtract.test(access)

    final override fun getAmount(): Int = amount

    override fun getCapacity(): Int = capacity

    override fun serialize(output: HTValueOutput) {
        output.putInt(HTConst.AMOUNT, getAmount())
    }

    override fun deserialize(input: HTValueInput) {
        input.getInt(HTConst.AMOUNT)?.let(::setAmountUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
