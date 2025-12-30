package hiiragi283.core.api.storage.fluid

import net.neoforged.neoforge.fluids.FluidStack

/**
 * この[FluidStack][this]を[HTFluidResourceType]に変換します。
 * @return [FluidStack.isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun FluidStack.toResource(): HTFluidResourceType? = HTFluidResourceType.of(this)
