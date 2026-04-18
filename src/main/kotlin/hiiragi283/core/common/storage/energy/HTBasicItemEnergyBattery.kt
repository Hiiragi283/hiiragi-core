package hiiragi283.core.common.storage.energy

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.common.storage.HTStorageValidators
import hiiragi283.core.util.HTStorageHelper
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

open class HTBasicItemEnergyBattery(
    private val capacity: Int,
    protected val canExtract: Predicate<HTStorageAccess>,
    protected val canInsert: Predicate<HTStorageAccess>,
    protected val container: ItemStack,
) : HTEnergyBattery.Basic(),
    HTContentListener.Empty,
    HTValueSerializable.Empty {
    companion object {
        @JvmStatic
        fun input(container: ItemStack, capacity: Int): HTBasicItemEnergyBattery =
            create(container, capacity, HTStorageAccess.NOT_EXTERNAL, HTStoragePredicates.alwaysTrue())

        @JvmStatic
        fun output(container: ItemStack, capacity: Int): HTBasicItemEnergyBattery =
            create(container, capacity, HTStoragePredicates.alwaysTrue(), HTStorageAccess.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            container: ItemStack,
            capacity: Int,
            canExtract: Predicate<HTStorageAccess> = HTStoragePredicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = HTStoragePredicates.alwaysTrue(),
        ): HTBasicItemEnergyBattery =
            HTBasicItemEnergyBattery(HTStorageValidators.validateCapacity(capacity), canExtract, canInsert, container)
    }

    override fun setAmount(amount: Int) {
        setAmountUnchecked(amount, true)
    }

    fun setAmountUnchecked(amount: Int, validate: Boolean = false) {
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
