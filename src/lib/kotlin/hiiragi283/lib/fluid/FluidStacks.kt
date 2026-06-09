package hiiragi283.lib.fluid

import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.right
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate

//    FluidStackTemplate    //

fun FluidStackTemplate?.createOrEmpty(): FluidStack = this?.create() ?: FluidStack.EMPTY

//    FluidStack    //

fun FluidStack.toTemplateOrNull(): FluidStackTemplate? = when {
    this.isEmpty -> null
    else -> FluidStackTemplate.fromNonEmptyStack(this)
}

fun FluidStack.toTemplateResult(): HTTextResult<FluidStackTemplate> = this.toTemplateOrNull()?.right() ?: HTTextResult("FluidStack must be non-empty")
