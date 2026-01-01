package hiiragi283.core.api.recipe.input

import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [液体][FluidStack]に対応した[RecipeInput]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
interface HTFluidRecipeInput : RecipeInput {
    fun getFluid(index: Int): FluidStack

    fun getFluidSize(): Int

    override fun isEmpty(): Boolean = super.isEmpty() && (0..<getFluidSize()).map(::getFluid).all(FluidStack::isEmpty)
}
