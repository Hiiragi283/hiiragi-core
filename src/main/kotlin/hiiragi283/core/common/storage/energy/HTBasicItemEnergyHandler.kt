package hiiragi283.core.common.storage.energy

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.amount.HTAmountSlot
import hiiragi283.core.api.storage.energy.HTEnergyHandler
import hiiragi283.core.support.storage.HTStorageValidators
import hiiragi283.core.util.HTStorageHelper
import java.util.function.Predicate
import net.minecraft.world.item.ItemStack

open class HTBasicItemEnergyHandler(
    private val capacity: Int,
    protected val canExtract: Predicate<HTStorageAccess>,
    protected val canInsert: Predicate<HTStorageAccess>,
    protected val container: ItemStack,
) : HTAmountSlot.Basic(),
    HTEnergyHandler,
    HTContentListener by HTContentListener.NOTHING,
    HTValueSerializable by HTValueSerializable.NOTHING {
    companion object {
        @JvmStatic
        fun input(container: ItemStack, capacity: Int): HTBasicItemEnergyHandler = create(container, capacity, HTStorageAccess.NOT_EXTERNAL, HTStoragePredicates.alwaysTrue())

        @JvmStatic
        fun output(container: ItemStack, capacity: Int): HTBasicItemEnergyHandler = create(container, capacity, HTStoragePredicates.alwaysTrue(), HTStorageAccess.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            container: ItemStack,
            capacity: Int,
            canExtract: Predicate<HTStorageAccess> = HTStoragePredicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = HTStoragePredicates.alwaysTrue(),
        ): HTBasicItemEnergyHandler = HTBasicItemEnergyHandler(HTStorageValidators.validateCapacity(capacity), canExtract, canInsert, container)
    }

    override fun setAmount(amount: Int) {
        setAmountUnchecked(amount, true)
    }

    protected fun setAmountUnchecked(amount: Int, validate: Boolean = false) {
        if (amount == 0) {
            if (getAmount() == 0) return
            setAmountInternal(0)
        } else if (!validate || amount > 0) {
            setAmountInternal(amount.coerceIn(0, getCapacity()))
        } else {
            error("Invalid amount for storage: $amount")
        }
        onContentsChanged()
    }

    private fun setAmountInternal(amount: Int) {
        HTStorageHelper.updateEnergy(container, amount)
    }

    override fun getAmount(): Int = HTStorageHelper.getEnergy(container)

    override fun getCapacity(): Int = capacity

    final override fun canInsert(access: HTStorageAccess): Boolean = this.canInsert.test(access)

    final override fun canExtract(access: HTStorageAccess): Boolean = this.canExtract.test(access)
}
