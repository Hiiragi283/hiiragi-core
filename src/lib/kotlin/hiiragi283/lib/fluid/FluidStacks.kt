package hiiragi283.lib.fluid

import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.toTextResult
import net.minecraft.core.Holder
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate

//    Fluid    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
@Suppress("DEPRECATION")
val Fluid.isEmpty: Boolean get() = this.builtInRegistryHolder().isEmpty

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
val Holder<Fluid>.isEmpty: Boolean get() = this.`is`(vanillaId("empty"))

//    FluidStackTemplate    //

/**
 * [FluidStackTemplate]が`null`の場合，[FluidStack.EMPTY]を返します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun FluidStackTemplate?.createOrEmpty(): FluidStack = this?.create() ?: FluidStack.EMPTY

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun FluidStackTemplate.transmuteCopy(newFluid: Fluid, newAmount: Int = this.amount()): FluidStackTemplate? = when {
    newFluid.isEmpty -> null
    else -> FluidStackTemplate(newFluid, newAmount, this.components())
}

//    FluidStack    //

/**
 * [FluidStack]を[FluidStackTemplate]に変換します。
 * @return [isEmpty]の場合は`null`
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

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun FluidStack.transmuteCopy(newFluid: Fluid, newAmount: Int = this.amount()): FluidStack = when {
    newFluid.isEmpty -> FluidStack.EMPTY
    else -> FluidStack(newFluid, newAmount, this.componentsPatch)
}
