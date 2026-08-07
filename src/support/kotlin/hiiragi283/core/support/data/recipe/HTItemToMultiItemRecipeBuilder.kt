@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.support.data.recipe

import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.progress.HTProgressData
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.world.item.crafting.Recipe

class HTItemToMultiItemRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) : HTMultiOutputRecipeBuilder<RECIPE>(prefix) {
    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder().apply(builderAction).buildSized()
    }

    override fun createRecipe(): RECIPE = factory.create(ingredient, results, progressData)

    //    Factory    //

    fun interface Factory<out RECIPE : Any> {
        fun create(ingredient: HTItemIngredient, results: List<HTChancedItemResult>, progressData: HTProgressData): RECIPE
    }
}
