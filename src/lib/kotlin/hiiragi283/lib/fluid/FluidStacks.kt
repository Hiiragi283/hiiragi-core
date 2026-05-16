package hiiragi283.lib.fluid

import hiiragi283.lib.data.buildDataPatch
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate

//    FluidStackTemplate    //

fun <T : Any> createFluidTemplate(fluid: Fluid, type: DataComponentType<T>, value: T, count: Int = 1): Result<FluidStackTemplate> = createFluidTemplate(fluid, count, buildDataPatch { set(type, value) })

fun createFluidTemplate(fluid: Fluid, count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): Result<FluidStackTemplate> = runCatching { FluidStackTemplate(fluid, count, patch) }

//    FluidStack    //

fun <T : Any> createFluidStack(fluid: Fluid, type: DataComponentType<T>, value: T, count: Int = 1): FluidStack = createFluidStack(fluid, count, buildDataPatch { set(type, value) })

/**
 * 指定した引数から新しい[FluidStack]のインスタンスを作成します。
 * @param fluid 液体の種類
 * @param count 液体の量
 * @param patch 適応するコンポーネントの差分
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
fun createFluidStack(fluid: Fluid, count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): FluidStack {
    val stack = FluidStack(fluid, count)
    if (stack.isEmpty) return FluidStack.EMPTY
    stack.applyComponents(patch)
    return stack
}
