package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipe
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 完成品に[液体][FluidStack]を扱うレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
interface HTFluidRecipe<INPUT : RecipeInput> : HTRecipe<INPUT> {
    /**
     * 完成品の[液体][FluidStack]を取得します。
     */
    fun assembleFluid(input: INPUT): FluidStack
}
