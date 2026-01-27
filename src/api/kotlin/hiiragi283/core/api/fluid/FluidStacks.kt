package hiiragi283.core.api.fluid

import hiiragi283.core.api.HTConst
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

//    FluidStack    //

/**
 * 指定した引数から新しい[FluidStack]のインスタンスを作成します。
 * @param fluid 液体の種類
 * @param amount 液体の量
 * @param patch 適応するコンポーネントの差分
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
fun createFluidStack(
    fluid: Fluid?,
    amount: Int = HTConst.DEFAULT_FLUID_AMOUNT,
    patch: DataComponentPatch = DataComponentPatch.EMPTY,
): FluidStack {
    if (fluid == null) return FluidStack.EMPTY
    val stack = FluidStack(fluid, amount)
    if (stack.isEmpty) return FluidStack.EMPTY
    stack.applyComponents(patch)
    return stack
}
