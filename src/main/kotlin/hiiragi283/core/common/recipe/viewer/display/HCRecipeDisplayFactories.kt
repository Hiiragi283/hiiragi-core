package hiiragi283.core.common.recipe.viewer.display

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.core.impl.recipe.HTInWorldRecipe
import net.minecraft.resources.ResourceLocation

data object HCRecipeDisplayFactories {
    @JvmStatic
    fun inWorld(holder: HTRecipeHolder<out HTInWorldRecipe>): HTRecipeDisplay.Simple {
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
    fun charging(holder: HTRecipeHolder<HCChargingRecipe>): HTProgressRecipeDisplay {
        val (id: ResourceLocation, recipe: HCChargingRecipe) = holder
        return HTProgressRecipeDisplay(
            id,
            HTRecipeContents.create {
                addInput(recipe.ingredient)
                addOutput(recipe.result)
            },
            HTProgressData.energy(recipe.energy),
        )
    }

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
