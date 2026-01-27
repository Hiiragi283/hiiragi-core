package hiiragi283.core.api.storage.fluid

import hiiragi283.core.api.storage.resource.HTResourceSlot

/**
 * [HTFluidResourceType]向けの[HTResourceSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
interface HTFluidTank : HTResourceSlot<HTFluidResourceType> {
    //    Basic    //

    /**
     * [HTFluidResourceType]向けの[HTResourceSlot.Basic]の拡張クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.4.0
     */
    abstract class Basic :
        HTResourceSlot.Basic<HTFluidResourceType>(),
        HTFluidTank {
        override fun toString(): String = "HTFluidTank(resource=${getResource()}, amount=${getAmount()}, capacity=${getCapacity()})"
    }
}
