package hiiragi283.core.api.fluid

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * アイテムのクラスを問わない[HTFluidLike]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
typealias HTSimpleFluidLike = HTFluidLike<Fluid>

/**
 * Hiiragi Seriesで使用されるインターフェースです。
 * @param FLUID [asFluid]で返されるアイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */

interface HTFluidLike<FLUID : Fluid> {
    fun asFluid(): FLUID

    /**
     * 新しい[FluidStack]のインスタンスを作成します。
     */
    fun toStack(amount: Int = FluidType.BUCKET_VOLUME, patch: DataComponentPatch = DataComponentPatch.EMPTY): FluidStack
}
