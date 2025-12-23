package hiiragi283.core.api.storage.energy

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.amount.HTAmountSlot
import kotlin.math.min

/**
 * エネルギーを保持する[HTAmountSlot.IntSized]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.energy.IEnergyContainer
 */
interface HTEnergyBattery :
    HTAmountSlot.IntSized,
    HTValueSerializable,
    HTContentListener {
    //    Basic    //

    /**
     * [HTEnergyBattery]の基本的な実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    abstract class Basic : HTEnergyBattery {
        /**
         * 指定した[amount]で中身を置換します。
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

        /**
         * このスロットに搬入できるか判定します。
         * @param access このスロットへのアクセスの種類
         * @return 搬入できる場合は`true`
         */
        protected open fun canInsert(access: HTStorageAccess): Boolean = true

        /**
         * このスロットから搬出できるか判定します。
         * @param access このスロットへのアクセスの種類
         * @return 搬出できる場合は`true`
         */
        protected open fun canExtract(access: HTStorageAccess): Boolean = true

        /**
         * 一度に搬入される量の上限を返します。
         * @param access このスロットへのアクセスの種類
         */
        protected open fun inputRate(access: HTStorageAccess): Int = Int.MAX_VALUE

        /**
         * 一度に搬出される量の上限を返します。
         * @param access このスロットへのアクセスの種類
         */
        protected open fun outputRate(access: HTStorageAccess): Int = Int.MAX_VALUE

        override fun toString(): String = "HTEnergyBattery(amount=${getAmount()}, capacity=${getCapacity()})"
    }
}
