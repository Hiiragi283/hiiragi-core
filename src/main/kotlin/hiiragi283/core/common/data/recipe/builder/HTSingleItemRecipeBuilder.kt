package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.impl.data.recipe.builder.HTAbstractSingleItemRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.StonecutterRecipe

class HTSingleItemRecipeBuilder(prefix: String) : HTAbstractSingleItemRecipeBuilder(prefix) {
    companion object {
        @JvmStatic
        inline fun stonecutting(output: RecipeOutput, builderAction: HTSingleItemRecipeBuilder.() -> Unit) {
            HTSingleItemRecipeBuilder("stonecutting").apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: Ingredient

    override fun createRecipe(): StonecutterRecipe = StonecutterRecipe(
        commonInfo(true),
        ingredient,
        result.template,
    )
}
