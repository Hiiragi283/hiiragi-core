package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.wrapOptional
import hiiragi283.core.impl.recipe.HTBasicForgingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HCForgingRecipeBuilder : HTProcessingRecipeBuilder(HTConst.FORGING) {
    companion object {
        @JvmStatic
        fun create(output: RecipeOutput, builderAction: HCForgingRecipeBuilder.() -> Unit) {
            HCForgingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var base: HTItemIngredient
    var addition: HTItemIngredient? = null
    val results: MutableList<HTItemResult> = mutableListOf()

    override fun getPrimalId(): ResourceLocation = results.first().getId()

    override fun createRecipe(): HTBasicForgingRecipe = HTBasicForgingRecipe(base, addition.wrapOptional(), results, time)
}
