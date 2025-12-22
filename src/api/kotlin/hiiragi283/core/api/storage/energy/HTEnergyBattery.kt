package hiiragi283.core.api.storage.energy

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.amount.HTAmountSlot
import kotlin.math.min

/**
 * @see mekanism.api.energy.IEnergyContainer
 */
interface HTEnergyBattery :
    HTAmountSlot.IntSized,
    HTValueSerializable,
    HTContentListener {
    //    Basic    //

    abstract class Basic : HTEnergyBattery {
        /**
         * 指定した電気量を代入します。
         */
        abstract fun setAmount(amount: Int)

        override fun insert(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (amount <= 0 || !canInsert(access)) return 0
            val needed: Int = min(inputRate(access), getNeeded())
            if (needed <= 0) return 0
            val toAdd: Int = min(amount, needed)
            if (action.execute()) {
                setAmount(getAmount() + toAdd)
                onContentsChanged()
            }
            return toAdd
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int {
            if (isEmpty() || amount <= 0 || !canExtract(access)) return 0
            val toRemove: Int = min(min(outputRate(access), getAmount()), amount)
            if (toRemove > 0 && action.execute()) {
                setAmount(getAmount() - toRemove)
                onContentsChanged()
            }
            return toRemove
        }

        protected open fun canInsert(access: HTStorageAccess): Boolean = true

        protected open fun canExtract(access: HTStorageAccess): Boolean = true

        protected open fun inputRate(access: HTStorageAccess): Int = Int.MAX_VALUE

        protected open fun outputRate(access: HTStorageAccess): Int = Int.MAX_VALUE

        override fun toString(): String = "HTEnergyBattery(amount=${getAmount()}, capacity=${getCapacity()})"
    }
}
