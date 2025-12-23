package hiiragi283.core.api.storage.holder

import hiiragi283.core.api.storage.energy.HTEnergyBattery
import net.minecraft.core.Direction

/**
 * [HTEnergyBattery]向けの[HTCapabilityHolder]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.capabilities.holder.energy.IEnergyContainerHolder
 */
interface HTEnergyBatteryHolder : HTCapabilityHolder {
    /**
     * 指定された[面][side]から[HTEnergyBattery]の一覧を取得します。
     */
    fun getEnergyBatteries(side: Direction?): List<HTEnergyBattery>
}
