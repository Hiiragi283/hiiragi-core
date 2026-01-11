package hiiragi283.core.api.storage.energy

import hiiragi283.core.api.HTDataSerializable
import hiiragi283.core.api.storage.amount.HTAmountSlot

/**
 * エネルギーを保持する[HTAmountSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.energy.IEnergyContainer
 */
interface HTEnergyBattery :
    HTAmountSlot,
    HTDataSerializable {
    //    Basic    //

    /**
     * [HTEnergyBattery]の基本的な実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    abstract class Basic :
        HTAmountSlot.Basic(),
        HTEnergyBattery {
        override fun toString(): String = "HTEnergyBattery(amount=${getAmount()}, capacity=${getCapacity()})"
    }
}
