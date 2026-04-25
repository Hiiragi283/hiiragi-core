package hiiragi283.core.common.recipe.viewer.display

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import net.minecraft.resources.ResourceLocation

data object HCRecipeDisplayFactories {
    @JvmStatic
    fun emptyingTank(holder: HTRecipeHolder<HCTankEmptyingRecipe>): HTRecipeDisplay.Simple {
        val (id: ResourceLocation, recipe: HCTankEmptyingRecipe) = holder
        return HTRecipeDisplay.Simple(
            id,
            HTRecipeContents.create {
                addInput(recipe.ingredient)
                addOutput(recipe.fluidResult)
                recipe.itemResult.ifPresent(::addOutput)
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
