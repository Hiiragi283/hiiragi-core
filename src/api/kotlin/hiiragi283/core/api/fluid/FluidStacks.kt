package hiiragi283.core.api.fluid

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

//    FluidStack    //

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun FluidStack(fluid: Fluid, amount: Int, patch: DataComponentPatch): FluidStack {
    val stack = FluidStack(fluid, amount)
    stack.applyComponents(patch)
    return stack
}

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun FluidStack(fluid: HTFluidLike<*>, amount: Int, patch: DataComponentPatch): FluidStack = FluidStack(fluid.asFluid(), amount, patch)
