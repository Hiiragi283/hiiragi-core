package hiiragi283.core.support.recipe.viewer.display

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.support.recipe.base.HTBasicItemOrFluidRecipe
import hiiragi283.core.support.recipe.base.HTBasicItemToItemRecipe
import hiiragi283.core.support.recipe.base.HTBasicItemToMultiItemRecipe
import net.minecraft.resources.ResourceLocation

data object HTRecipeDisplayFactories {
    @JvmStatic
    fun itemToItem(holder: HTRecipeHolder<HTBasicItemToItemRecipe>): HTProgressRecipeDisplay = progress(holder) { recipe ->
        addInput(recipe.ingredient)
        addOutput(recipe.result)
    }

    @JvmStatic
    fun itemToMultiItem(holder: HTRecipeHolder<HTBasicItemToMultiItemRecipe>): HTProgressRecipeDisplay = progress(holder) { recipe ->
        addInput(recipe.ingredient)
        recipe.results.forEach(::addOutput)
    }

    @JvmStatic
    fun itemOrFluid(holder: HTRecipeHolder<HTBasicItemOrFluidRecipe>): HTProgressRecipeDisplay = progress(holder) { recipe ->
        recipe.ingredient.mapLeft { addInput(it) }.mapRight { addInput(it) }
        recipe.result.mapLeft { addOutput(it) }.mapRight { addOutput(it) }
    }

    @JvmStatic
    inline fun <RECIPE : HTProgressRecipe.Simple<*>> progress(
        holder: HTRecipeHolder<RECIPE>,
        builderAction: HTRecipeContents.Builder.(RECIPE) -> Unit,
    ): HTProgressRecipeDisplay {
        val (id: ResourceLocation, recipe: RECIPE) = holder
        return HTProgressRecipeDisplay(
            id,
            HTRecipeContents.create { this.builderAction(recipe) },
            recipe.progressData,
        )
    }
}
