package hiiragi283.core.api.fluid

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.buildDataPatch
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

//    FluidStack    //

fun <T : Any> createFluidStack(
    fluid: Fluid?,
    type: DataComponentType<T>,
    value: T,
    amount: Int = HTConst.DEFAULT_FLUID_AMOUNT,
): FluidStack = createFluidStack(fluid, amount, buildDataPatch { set(type, value) })

fun createFluidStack(
    fluid: Fluid?,
    amount: Int = HTConst.DEFAULT_FLUID_AMOUNT,
    patch: DataComponentPatch = DataComponentPatch.EMPTY,
): FluidStack {
    if (fluid == null) return FluidStack.EMPTY
    val stack = FluidStack(fluid, amount)
    stack.applyComponents(patch)
    return stack
}
