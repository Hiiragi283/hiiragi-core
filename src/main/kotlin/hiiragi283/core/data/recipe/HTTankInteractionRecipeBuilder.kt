@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.data.recipe

import hiiragi283.core.api.HCConstants
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.lib.data.recipe.HTItemResultBuilder
import hiiragi283.lib.data.recipe.HTRecipeBuilder
import hiiragi283.lib.data.recipe.IngredientBuilder
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
        @PublishedApi internal var ingredient: Ingredient by HTDelegates.onceInitialize()

        @PublishedApi internal var fluidResult: HTFluidResult by HTDelegates.onceInitialize()

        @PublishedApi internal var itemResult: HTItemResult? = null

        operator fun Ingredient.unaryPlus() {
            ingredient = this
        }

        operator fun HTFluidResult.unaryPlus() {
            fluidResult = this
        }

        operator fun HTItemResult.unaryPlus() {
            itemResult = this
        }

        inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            ingredient = IngredientBuilder().apply(builderAction).build()
        }

        inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            itemResult = HTItemResultBuilder().apply(builderAction).build()
        }

        override fun getPrimalId(): Identifier = fluidResult.getId()

        override fun createRecipe(): HCTankEmptyingRecipe = HCTankEmptyingRecipe(ingredient, fluidResult, itemResult.toOption())
    }

    class Filling : HTRecipeBuilder<HCTankFillingRecipe>(HCConstants.FILLING) {
        @PublishedApi internal var itemIngredient: Ingredient by HTDelegates.onceInitialize()

        @PublishedApi internal var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

        @PublishedApi internal var itemResult: HTItemResult by HTDelegates.onceInitialize()

        operator fun Ingredient.unaryPlus() {
            itemIngredient = this
        }

        operator fun HTFluidIngredient.unaryPlus() {
            fluidIngredient = this
        }

        operator fun HTItemResult.unaryPlus() {
            itemResult = this
        }

        inline fun itemIngredient(builderAction: IngredientBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            itemIngredient = IngredientBuilder().apply(builderAction).build()
        }

        inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            itemResult = HTItemResultBuilder().apply(builderAction).build()
        }

        override fun getPrimalId(): Identifier = itemResult.getId()

        override fun createRecipe(): HCTankFillingRecipe = HCTankFillingRecipe(itemIngredient, fluidIngredient, itemResult)
    }
}
