package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.base.HTFluidRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [RecipeInput]と[HTRecipe]を束ねたクラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTHandledRecipe<INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>> private constructor(val input: INPUT, val recipe: RECIPE) {
    companion object {
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>> create(input: INPUT, recipe: RECIPE): HTHandledRecipe<INPUT, RECIPE>? = when {
            recipe.test(input) -> HTHandledRecipe(input, recipe)
            else -> null
        }
    }

    /**
     * レシピの完成品を取得します。
     */
    fun assemble(preview: Boolean): ItemStack = recipe.assemble(input, preview)
}

//    Extensions    //

/**
 * [HTFluidRecipe.assembleFluid]に基づいて完成品を取得します。
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <INPUT : RecipeInput, RECIPE> HTHandledRecipe<INPUT, RECIPE>.assembleFluid(): FluidStack where RECIPE : HTRecipe<INPUT>, RECIPE : HTFluidRecipe<INPUT> =
    this.recipe.assembleFluid(this.input)
