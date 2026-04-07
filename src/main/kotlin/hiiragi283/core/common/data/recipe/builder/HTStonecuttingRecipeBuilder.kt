package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.holder.HTIngredientHolder
import hiiragi283.core.api.data.recipe.builder.HTStackRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.StonecutterRecipe

class HTStonecuttingRecipeBuilder : HTStackRecipeBuilder("stonecutting") {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTStonecuttingRecipeBuilder.() -> Unit) {
            HTStonecuttingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var group: String? = null
    val ingredient = HTIngredientHolder.Single()

    override fun createRecipe(): StonecutterRecipe = StonecutterRecipe(
        group ?: "",
        ingredient.ingredient,
        resultStack.stack,
    )
}
