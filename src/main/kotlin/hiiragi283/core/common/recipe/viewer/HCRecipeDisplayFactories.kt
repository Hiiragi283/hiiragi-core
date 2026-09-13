package hiiragi283.core.common.recipe.viewer

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.core.support.recipe.base.HTInWorldRecipe
import hiiragi283.core.support.recipe.viewer.display.HTRecipeDisplayFactories
import net.minecraft.resources.ResourceLocation

data object HCRecipeDisplayFactories {
    @JvmStatic
    fun inWorld(holder: HTRecipeHolder<HTInWorldRecipe>): HTRecipeDisplay.Simple {
        val (id: ResourceLocation, recipe: HTInWorldRecipe) = holder
        return HTRecipeDisplay.Simple(
            id,
            HTRecipeContents.create {
                addInput(recipe.ingredient)
                addOutput(recipe.result)
            },
        )
    }

    @JvmStatic
    fun charging(holder: HTRecipeHolder<HCChargingRecipe>): HTProgressRecipeDisplay = HTRecipeDisplayFactories.progress(holder) {
        addInput(it.ingredient)
        addOutput(it.result)
    }

    @JvmStatic
    fun emptyingTank(holder: HTRecipeHolder<HCTankEmptyingRecipe>): HTRecipeDisplay.Simple {
        val (id: ResourceLocation, recipe: HCTankEmptyingRecipe) = holder
        return HTRecipeDisplay.Simple(
            id,
            HTRecipeContents.create {
                addInput(recipe.ingredient)
                addOutput(recipe.fluidResult)
                recipe.itemResult.onSome(::addOutput)
            },
        )
    }

    @JvmStatic
    fun fillingTank(holder: HTRecipeHolder<HCTankFillingRecipe>): HTRecipeDisplay.Simple {
        val (id: ResourceLocation, recipe: HCTankFillingRecipe) = holder
        return HTRecipeDisplay.Simple(
            id,
            HTRecipeContents.create {
                addInput(recipe.itemIngredient)
                addInput(recipe.fluidIngredient)
                addOutput(recipe.result)
            },
        )
    }
}
