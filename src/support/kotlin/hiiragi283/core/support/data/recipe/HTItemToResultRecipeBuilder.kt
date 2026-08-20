@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.support.data.recipe

import hiiragi283.core.api.data.recipe.HTProgressRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

abstract class HTItemToResultRecipeBuilder<out RECIPE : Recipe<*>, RESULT : HTIdLike>(prefix: String, private val factory: Factory<RESULT, RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    protected var result: RESULT by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder().apply(builderAction).buildSized()
    }

    operator fun RESULT.unaryPlus() {
        result = this
    }

    final override fun getPrimalId(): ResourceLocation = result.getId()

    final override fun createRecipe(): RECIPE = factory.create(ingredient, result, progressData)

    //    Factory    //

    fun interface Factory<RESULT : Any, out RECIPE : Any> {
        fun create(ingredient: HTItemIngredient, result: RESULT, progressData: HTProgressData): RECIPE
    }
}
