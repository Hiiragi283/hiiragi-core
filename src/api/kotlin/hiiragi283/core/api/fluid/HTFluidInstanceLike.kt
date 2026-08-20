package hiiragi283.core.api.fluid

import net.minecraft.core.component.DataComponentPatch
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * [FluidStack]に変換可能なオブジェクトを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
interface HTFluidInstanceLike {
    /**
     * 新しい[FluidStack]のインスタンスを作成します。
     */
    fun toStack(amount: Int = FluidType.BUCKET_VOLUME, patch: DataComponentPatch = DataComponentPatch.EMPTY): FluidStack
}
