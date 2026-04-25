package hiiragi283.core.impl.recipe.viewer.display

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.viewer.display.HTProcessingRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.impl.recipe.HTBasicDoubleMultiOutputRecipe
import hiiragi283.core.impl.recipe.HTBasicSingleItemRecipe
import hiiragi283.core.impl.recipe.HTBasicSingleMultiOutputRecipe
import net.minecraft.resources.ResourceLocation

data object HTRecipeDisplayFactories {
    @JvmStatic
    fun singleItem(holder: HTRecipeHolder<out HTBasicSingleItemRecipe>): HTProcessingRecipeDisplay = processing(holder) { recipe ->
        addInput(recipe.ingredient)
        addOutput(recipe.result)
    }

    @JvmStatic
    fun singleMultiItem(holder: HTRecipeHolder<out HTBasicSingleMultiOutputRecipe>): HTProcessingRecipeDisplay =
        processing(holder) { recipe ->
            addInput(recipe.ingredient)
            recipe.results.forEach(::addOutput)
        }

    @JvmStatic
    fun doubleMultiItem(holder: HTRecipeHolder<out HTBasicDoubleMultiOutputRecipe>): HTProcessingRecipeDisplay =
        processing(holder) { recipe ->
            addInput(recipe.base)
            recipe.addition.ifPresent(::addInput)
            recipe.results.forEach(::addOutput)
        }

    @JvmStatic
    private inline fun <RECIPE : HTProcessingRecipe<*>> processing(
        holder: HTRecipeHolder<RECIPE>,
        builderAction: HTRecipeContents.Builder.(RECIPE) -> Unit,
    ): HTProcessingRecipeDisplay {
        val (id: ResourceLocation, recipe: RECIPE) = holder
        return HTProcessingRecipeDisplay(id, HTRecipeContents.create { this.builderAction(recipe) }, recipe.time)
    }
}
