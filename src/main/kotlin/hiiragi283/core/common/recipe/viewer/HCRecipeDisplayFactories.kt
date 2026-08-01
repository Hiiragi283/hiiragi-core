package hiiragi283.core.common.recipe.viewer

import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.core.common.recipe.VanillaBrewingRecipe
import hiiragi283.core.common.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.core.support.recipe.base.HTInWorldRecipe
import hiiragi283.core.util.HCPotionFluidHelper
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
    fun brewing(holder: HTRecipeHolder<HTItemOrFluidRecipe>): HTProgressRecipeDisplay? {
        val (id: ResourceLocation, recipe: HTItemOrFluidRecipe) = holder
        return when (recipe) {
            is VanillaBrewingRecipe -> HTProgressRecipeDisplay(
                id,
                HTRecipeContents.create {
                    addInput(recipe.potionFrom.let(::HTPotionFluidIngredient).stacks.toList())
                    addInput(recipe.ingredient)
                    addOutput(recipe.potionTo.let(::BottledPotionContents).let(HCPotionFluidHelper::createFluid))
                },
                HTProgressData.Time(200),
            )
            is HCBrewingRecipe -> HTProgressRecipeDisplay(
                id,
                HTRecipeContents.create {
                    addInput(recipe.itemIngredient)
                    addInput(recipe.fluidIngredient)
                    addOutput(recipe.result)
                },
                recipe.progressData,
            )
            else -> null
        }
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
