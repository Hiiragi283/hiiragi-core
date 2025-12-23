package hiiragi283.core.api.storage.energy

import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * [IEnergyStorage]に基づいた[HTSidedEnergyStorage]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.energy.IMekanismStrictEnergyHandler
 */
fun interface HTEnergyHandler : HTSidedEnergyStorage {
    /**
     * このハンドラが有効か判定します。
     */
    fun hasEnergyStorage(): Boolean = true

    /**
     * 指定した[面][side]から[HTEnergyBattery]を取得します。
     * @return 取得できない場合は`null`
     */
    fun getEnergyBattery(side: Direction?): HTEnergyBattery?

    override fun receiveEnergy(toReceive: Int, action: HTStorageAction, side: Direction?): Int =
        getEnergyBattery(side)?.insert(toReceive, action, HTStorageAccess.EXTERNAL) ?: 0

    override fun extractEnergy(toExtract: Int, action: HTStorageAction, side: Direction?): Int =
        getEnergyBattery(side)?.extract(toExtract, action, HTStorageAccess.EXTERNAL) ?: 0

    override fun getEnergyStored(side: Direction?): Int = getEnergyBattery(side)?.getAmount() ?: 0

    override fun getMaxEnergyStored(side: Direction?): Int = getEnergyBattery(side)?.getCapacity() ?: 0
}
