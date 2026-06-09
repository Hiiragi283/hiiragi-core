@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.data.recipe

import hiiragi283.core.api.HCConstants
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.lib.data.recipe.HTRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.toOption
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Ingredient

data object HTTankInteractionRecipeBuilder {
    @JvmStatic
    inline fun emptying(builderAction: Emptying.() -> Unit): Emptying {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return Emptying().apply(builderAction)
    }

    @JvmStatic
    inline fun filling(builderAction: Filling.() -> Unit): Filling {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return Filling().apply(builderAction)
    }

    class Emptying : HTRecipeBuilder<HCTankEmptyingRecipe>(HCConstants.EMPTYING) {
        var ingredient: Ingredient by HTDelegates.onceInitialize()
        var fluidResult: HTFluidResult by HTDelegates.onceInitialize()
        var itemResult: HTItemResult? = null

        override fun getPrimalId(): Identifier = fluidResult.getId()

        override fun createRecipe(): HCTankEmptyingRecipe = HCTankEmptyingRecipe(ingredient, fluidResult, itemResult.toOption())
    }

    class Filling : HTRecipeBuilder<HCTankFillingRecipe>(HCConstants.FILLING) {
        var itemIngredient: Ingredient by HTDelegates.onceInitialize()
        var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()
        var itemResult: HTItemResult by HTDelegates.onceInitialize()

        override fun getPrimalId(): Identifier = itemResult.getId()

        override fun createRecipe(): HCTankFillingRecipe = HCTankFillingRecipe(itemIngredient, fluidIngredient, itemResult)
    }
}
