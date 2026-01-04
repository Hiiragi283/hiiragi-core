package hiiragi283.core.api.recipe.input

import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [液体][FluidStack]に対応した[RecipeInput]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
interface HTFluidRecipeInput : RecipeInput {
    /**
     * 指定した[インデックス][index]から[液体][FluidStack]を取得します。
     */
    fun getFluid(index: Int): FluidStack

    /**
     * このクラスが保持する液体の種類数を取得します。
     */
    fun getFluidSize(): Int

    override fun isEmpty(): Boolean = super.isEmpty() && (0..<getFluidSize()).map(::getFluid).all(FluidStack::isEmpty)
}
