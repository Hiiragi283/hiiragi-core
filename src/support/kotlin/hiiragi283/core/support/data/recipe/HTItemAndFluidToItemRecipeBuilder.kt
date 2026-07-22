@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.support.data.recipe

import hiiragi283.core.api.data.recipe.FluidIngredientBuilder
import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTProgressRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTItemAndFluidToItemRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    @PublishedApi internal var itemIngredient: HTItemIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var result: HTItemResult by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
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
        itemIngredient = IngredientBuilder().apply(builderAction).buildSized()
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

    override fun createRecipe(): RECIPE = factory.create(itemIngredient, fluidIngredient, result, progressData)

    //    Factory    //

    fun interface Factory<out RECIPE : Any> {
        fun create(itemIngredient: HTItemIngredient, fluidIngredient: HTFluidIngredient, result: HTItemResult, progressData: HTProgressData): RECIPE
    }
}
