package hiiragi283.core.api.storage.holder

import hiiragi283.core.api.storage.fluid.HTFluidTank
import net.minecraft.core.Direction

/**
 * [HTFluidTank]向けの[HTCapabilityHolder]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.capabilities.holder.fluid.IFluidTankHolder
 */
interface HTFluidTankHolder : HTCapabilityHolder {
    /**
     * 指定された[面][side]から[HTFluidTank]の一覧を取得します。
     */
    fun getFluidTank(side: Direction?): List<HTFluidTank>
}
