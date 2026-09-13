package hiiragi283.core.api.fluid

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

//    FluidStack    //

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun FluidStack(fluid: Fluid?, amount: Int, patch: DataComponentPatch): FluidStack {
    if (fluid == null) return FluidStack.EMPTY
    val stack = FluidStack(fluid, amount)
    stack.applyComponents(patch)
    return stack
}
