@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.FluidIngredientBuilder
import hiiragi283.core.api.data.recipe.HTFluidResultBuilder
import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.api.util.toOption
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

object HTTankInteractionRecipeBuilder {
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

    class Emptying : HTRecipeBuilder<HCTankEmptyingRecipe>(HTConst.EMPTYING) {
        var ingredient: Ingredient by HTDelegates.onceInitialize()
        var fluidResult: HTFluidResult by HTDelegates.onceInitialize()
        var itemResult: HTItemResult? = null

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

        inline fun fluidResult(builderAction: HTFluidResultBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            fluidResult = HTFluidResultBuilder().apply(builderAction).build()
        }

        inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            itemResult = HTItemResultBuilder().apply(builderAction).build()
        }

        override fun getPrimalId(): ResourceLocation = fluidResult.getId()

        override fun createRecipe(): HCTankEmptyingRecipe = HCTankEmptyingRecipe(ingredient, fluidResult, itemResult.toOption())
    }

    class Filling : HTRecipeBuilder<HCTankFillingRecipe>(HTConst.FILLING) {
        @PublishedApi internal var itemIngredient: Ingredient by HTDelegates.onceInitialize()

        @PublishedApi internal var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

        @PublishedApi internal var result: HTItemResult by HTDelegates.onceInitialize()

        operator fun Ingredient.unaryPlus() {
            itemIngredient = this
        }

        operator fun HTFluidIngredient.unaryPlus() {
            fluidIngredient = this
        }

        operator fun HTItemResult.unaryPlus() {
            result = this
        }

        inline fun itemIngredient(builderAction: IngredientBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            itemIngredient = IngredientBuilder().apply(builderAction).build()
        }

        inline fun fluidIngredient(builderAction: FluidIngredientBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            fluidIngredient = FluidIngredientBuilder().apply(builderAction).buildSized()
        }

        inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            result = HTItemResultBuilder().apply(builderAction).build()
        }

        override fun getPrimalId(): ResourceLocation = result.getId()

        override fun createRecipe(): HCTankFillingRecipe = HCTankFillingRecipe(itemIngredient, fluidIngredient, result)
    }
}
