package hiiragi283.core.common.storage.energy

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStoragePredicates
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Mth
import java.util.function.Predicate

/**
 * @see mekanism.common.capabilities.energy.BasicEnergyContainer
 */
open class HTBasicEnergyBattery(
    private val capacity: Int,
    protected val canExtract: Predicate<HTStorageAccess>,
    protected val canInsert: Predicate<HTStorageAccess>,
) : HTEnergyBattery.Basic() {
    companion object {
        @JvmStatic
        fun input(capacity: Int): HTBasicEnergyBattery = create(capacity, HTStorageAccess.NOT_EXTERNAL, HTStoragePredicates.alwaysTrue())

        @JvmStatic
        fun output(capacity: Int): HTBasicEnergyBattery = create(capacity, HTStoragePredicates.alwaysTrue(), HTStorageAccess.INTERNAL_ONLY)

        @JvmStatic
        fun create(
            capacity: Int,
            canExtract: Predicate<HTStorageAccess> = HTStoragePredicates.alwaysTrue(),
            canInsert: Predicate<HTStorageAccess> = HTStoragePredicates.alwaysTrue(),
        ): HTBasicEnergyBattery = HTBasicEnergyBattery(capacity, canExtract, canInsert)
    }

    @JvmField
    protected var amount: Int = 0

    override fun setAmount(amount: Int) {
        setAmountUnchecked(amount, true)
    }

    fun setAmountUnchecked(amount: Int, validate: Boolean = false) {
        if (amount == 0) {
            if (this.amount == 0) return
            this.amount = 0
        } else if (!validate || amount > 0) {
            this.amount = Mth.clamp(amount, 0, getCapacity())
        } else {
            error("Invalid amount for storage: $amount")
        }
    }

    final override fun canInsert(access: HTStorageAccess): Boolean = this.canInsert.test(access)

    final override fun canExtract(access: HTStorageAccess): Boolean = this.canExtract.test(access)

    override fun getAmount(): Int = amount

    override fun getCapacity(): Int = capacity

    override fun serializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        nbt.putInt(HTConst.AMOUNT, getAmount())
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        nbt.getInt(HTConst.AMOUNT).let(::setAmountUnchecked)
    }
}
