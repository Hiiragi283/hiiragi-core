package hiiragi283.core.impl.recipe.viewer.display

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.impl.recipe.HTBasicDoubleMultiOutputRecipe
import hiiragi283.core.impl.recipe.HTBasicSingleItemRecipe
import hiiragi283.core.impl.recipe.HTBasicSingleMultiOutputRecipe
import net.minecraft.resources.ResourceLocation

data object HTRecipeDisplayFactories {
    @JvmStatic
    fun singleItem(holder: HTRecipeHolder<out HTBasicSingleItemRecipe>): HTProgressRecipeDisplay = ticking(holder) { recipe ->
        addInput(recipe.ingredient)
        addOutput(recipe.result)
    }

    @JvmStatic
    fun singleMultiItem(holder: HTRecipeHolder<out HTBasicSingleMultiOutputRecipe>): HTProgressRecipeDisplay = ticking(holder) { recipe ->
        addInput(recipe.ingredient)
        recipe.results.forEach(::addOutput)
    }

    @JvmStatic
    fun doubleMultiItem(holder: HTRecipeHolder<out HTBasicDoubleMultiOutputRecipe>): HTProgressRecipeDisplay = ticking(holder) { recipe ->
        addInput(recipe.base)
        recipe.addition.ifPresent(::addInput)
        recipe.results.forEach(::addOutput)
    }

    @JvmStatic
    inline fun <RECIPE : HTProgressRecipe.Ticking<*>> ticking(
        holder: HTRecipeHolder<RECIPE>,
        builderAction: HTRecipeContents.Builder.(RECIPE) -> Unit,
    ): HTProgressRecipeDisplay {
        val (id: ResourceLocation, recipe: RECIPE) = holder
        return HTProgressRecipeDisplay(
            id,
            HTRecipeContents.create { this.builderAction(recipe) },
            HTProgressData.time(recipe.time),
        )
    }

    @JvmStatic
    inline fun <RECIPE : HTProgressRecipe.Energized<*>> energized(
        holder: HTRecipeHolder<RECIPE>,
        builderAction: HTRecipeContents.Builder.(RECIPE) -> Unit,
    ): HTProgressRecipeDisplay {
        val (id: ResourceLocation, recipe: RECIPE) = holder
        return HTProgressRecipeDisplay(
            id,
            HTRecipeContents.create { this.builderAction(recipe) },
            HTProgressData.energy(recipe.energy),
        )
    }
}
