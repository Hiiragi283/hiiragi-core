package hiiragi283.core.impl.recipe.viewer.display

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.impl.recipe.HTItemToItemRecipe
import hiiragi283.core.impl.recipe.HTItemToMultiItemRecipe
import net.minecraft.resources.ResourceLocation

data object HTRecipeDisplayFactories {
    @JvmStatic
    fun itemToItem(holder: HTRecipeHolder<out HTItemToItemRecipe>): HTProgressRecipeDisplay = progress(holder) { recipe ->
        addInput(recipe.ingredient)
        addOutput(recipe.result)
    }

    @JvmStatic
    fun itemToMultiItem(holder: HTRecipeHolder<out HTItemToMultiItemRecipe>): HTProgressRecipeDisplay = progress(holder) { recipe ->
        addInput(recipe.ingredient)
        recipe.results.forEach(::addOutput)
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
