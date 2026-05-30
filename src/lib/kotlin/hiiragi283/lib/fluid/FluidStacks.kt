package hiiragi283.lib.fluid

import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.getOrElse
import hiiragi283.lib.util.right
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

//    FluidStackTemplate    //

fun <T : Any> createFluidTemplate(fluid: Fluid, type: DataComponentType<T>, value: T, amount: Int = FluidType.BUCKET_VOLUME): HTTextResult<FluidStackTemplate> = createFluidTemplate(fluid, amount, buildDataPatch { set(type, value) })

fun createFluidTemplate(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME, patch: DataComponentPatch = DataComponentPatch.EMPTY): HTTextResult<FluidStackTemplate> = when (fluid) {
    Fluids.EMPTY -> HTTextResult("Fluid must be non-empty")
    else -> FluidStackTemplate(fluid, amount, patch).right()
}

fun FluidStackTemplate?.createOrEmpty(): FluidStack = this?.create() ?: FluidStack.EMPTY

//    FluidStack    //

fun <T : Any> createFluidStack(fluid: Fluid, type: DataComponentType<T>, value: T, amount: Int = FluidType.BUCKET_VOLUME): FluidStack = createFluidStack(fluid, amount, buildDataPatch { set(type, value) })

/**
 * 指定した引数から新しい[FluidStack]のインスタンスを作成します。
 * @param fluid 液体の種類
 * @param amount 液体の量
 * @param patch 適応するコンポーネントの差分
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
fun createFluidStack(fluid: Fluid, amount: Int = FluidType.BUCKET_VOLUME, patch: DataComponentPatch = DataComponentPatch.EMPTY): FluidStack = createFluidTemplate(fluid, amount, patch).map(FluidStackTemplate::create).getOrElse { FluidStack.EMPTY }

fun FluidStack.toTemplateOrNull(): FluidStackTemplate? = when {
    this.isEmpty -> null
    else -> FluidStackTemplate.fromNonEmptyStack(this)
}

fun FluidStack.toTemplateResult(): HTTextResult<FluidStackTemplate> = this.toTemplateOrNull()?.right() ?: HTTextResult("FluidStack must be non-empty")
