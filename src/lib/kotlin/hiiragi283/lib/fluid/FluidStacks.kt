package hiiragi283.lib.fluid

import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.toTextResult
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate

//    FluidStackTemplate    //

/**
 * [FluidStackTemplate]が`null`の場合，[FluidStack.EMPTY]を返します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidStackTemplate?.createOrEmpty(): FluidStack = this?.create() ?: FluidStack.EMPTY

//    FluidStack    //

/**
 * [FluidStack]を[FluidStackTemplate]に変換します。
 * @return [FluidStack.isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidStack.toTemplateOrNull(): FluidStackTemplate? = when {
    this.isEmpty -> null
    else -> FluidStackTemplate.fromNonEmptyStack(this)
}

/**
 * [FluidStack]を[FluidStackTemplate]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidStack.toTemplateResult(): HTTextResult<FluidStackTemplate> = this.toTemplateOrNull().toTextResult { "FluidStack must be non-empty" }
