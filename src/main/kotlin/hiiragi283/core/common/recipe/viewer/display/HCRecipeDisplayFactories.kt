package hiiragi283.core.common.recipe.viewer.display

import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.core.common.recipe.HTInWorldRecipe
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.lib.recipe.viewer.display.HTRecipeDisplay

data object HCRecipeDisplayFactories {
    @JvmStatic
    fun inWorld(holder: HTRecipeHolder<HTInWorldRecipe>): HTRecipeDisplay.Simple {
        val (key: RecipeKey, recipe: HTInWorldRecipe) = holder
        return HTRecipeDisplay(key) {
            addInput(recipe.ingredient)
            addOutput(recipe.result)
        }
    }

    @JvmStatic
    fun charging(holder: HTRecipeHolder<HCChargingRecipe>): HTProgressRecipeDisplay {
        val (key: RecipeKey, recipe: HCChargingRecipe) = holder
        return HTProgressRecipeDisplay(key, HTProgressData.energy(recipe.energy)) {
            addInput(recipe.ingredient)
            addOutput(recipe.result)
        }
    }

    @JvmStatic
    fun emptyingTank(holder: HTRecipeHolder<HCTankEmptyingRecipe>): HTRecipeDisplay.Simple {
        val (key: RecipeKey, recipe: HCTankEmptyingRecipe) = holder
        return HTRecipeDisplay(key) {
            addInput(recipe.ingredient)
            addOutput(recipe.fluidResult)
            recipe.itemResult.onSome(::addOutput)
        }
    }

    @JvmStatic
    fun fillingTank(holder: HTRecipeHolder<HCTankFillingRecipe>): HTRecipeDisplay.Simple {
        val (key: RecipeKey, recipe: HCTankFillingRecipe) = holder
        return HTRecipeDisplay(key) {
            addInput(recipe.itemIngredient)
            addInput(recipe.fluidIngredient)
            addOutput(recipe.result)
        }
    }
}
