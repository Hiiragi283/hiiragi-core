package hiiragi283.core.api.storage.energy

import hiiragi283.core.api.serialization.value.HTValueSerializable
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.api.storage.amount.HTAmountSlot
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * Hiiragi Seriesで使用される[IEnergyStorage]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTEnergyHandler :
    HTAmountSlot,
    HTValueSerializable,
    IEnergyStorage {
    //    IEnergyStorage    //

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int = insert(toReceive, HTStorageAction.of(simulate), HTStorageAccess.EXTERNAL)

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int = extract(toExtract, HTStorageAction.of(simulate), HTStorageAccess.EXTERNAL)

    @Deprecated("Use 'getAmount()' instead", ReplaceWith("this.getAmount()"))
    override fun getEnergyStored(): Int = getAmount()

    @Deprecated("Use 'getCapacity()' instead", ReplaceWith("this.getCapacity()"))
    override fun getMaxEnergyStored(): Int = getCapacity()

    @Deprecated("Not used", level = DeprecationLevel.ERROR)
    override fun canExtract(): Boolean = true

    @Deprecated("Not used", level = DeprecationLevel.ERROR)
    override fun canReceive(): Boolean = true
}
