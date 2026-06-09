@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.recipe.viewer.display

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.base.impl.HTBasicItemToChancedItemsRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

data object HTRecipeDisplayFactories {
    @JvmStatic
    fun itemToChancedItems(holder: HTRecipeHolder<HTBasicItemToChancedItemsRecipe>): HTProgressRecipeDisplay = progress(holder) { recipe ->
        addInput(recipe.ingredient)
        recipe.results.forEach(::addOutput)
    }

    @JvmStatic
    inline fun <RECIPE : HTProgressRecipe.Simple<*>> progress(holder: HTRecipeHolder<RECIPE>, builderAction: HTRecipeContents.Builder.(RECIPE) -> Unit): HTProgressRecipeDisplay {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        val (key: RecipeKey, recipe: RECIPE) = holder
        return HTProgressRecipeDisplay(key, recipe.progressData) { this.builderAction(recipe) }
    }
}
