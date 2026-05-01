package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.holder.HTIngredientHolder
import hiiragi283.core.api.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCForgingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HCForgingRecipeBuilder : HTProgressRecipeBuilder(HTConst.FORGING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HCForgingRecipeBuilder.() -> Unit) {
            HCForgingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var primary: HTItemIngredient
    val secondary: HTIngredientHolder.Single = HTIngredientHolder.Single()
    lateinit var result: HTItemResult

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): Recipe<*> = HCForgingRecipe(primary, secondary.ingredient, result, progressData)
}
