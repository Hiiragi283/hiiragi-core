package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTMinMaxRange
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.common.recipe.HCMeltingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HCMeltingRecipeBuilder : HTProcessingRecipeBuilder(HTConst.MELTING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HCMeltingRecipeBuilder.() -> Unit) {
            HCMeltingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    lateinit var result: HTFluidResult
    var heatRange: HTMinMaxRange<Int> = HTMinMaxRange.atLeast(300 + 100)

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HCMeltingRecipe = HCMeltingRecipe(ingredient, result, heatRange, time)
}
