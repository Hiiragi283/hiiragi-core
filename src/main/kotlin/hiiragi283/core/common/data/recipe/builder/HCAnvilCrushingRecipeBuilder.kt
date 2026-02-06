package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HCAnvilCrushingRecipeBuilder : HTRecipeBuilder(HTConst.ANVIL_CRUSHING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        fun create(output: RecipeOutput, builderAction: HCAnvilCrushingRecipeBuilder.() -> Unit) {
            HCAnvilCrushingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    lateinit var result: HTItemResult
    var extraResult: HTChancedItemResult? = null

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HCAnvilCrushingRecipe = HCAnvilCrushingRecipe(
        ingredient,
        result,
        extraResult,
    )
}
