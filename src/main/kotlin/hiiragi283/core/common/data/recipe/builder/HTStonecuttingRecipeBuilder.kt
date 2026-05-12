package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.impl.data.recipe.builder.HTStackRecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.StonecutterRecipe

class HTStonecuttingRecipeBuilder : HTStackRecipeBuilder("stonecutting") {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTStonecuttingRecipeBuilder.() -> Unit) {
            HTStonecuttingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var group: String? = null
    var ingredient: Ingredient by HTDelegates.onceInitialize()

    override fun createRecipe(): StonecutterRecipe = StonecutterRecipe(
        group ?: "",
        ingredient,
        resultStack.stack,
    )
}
