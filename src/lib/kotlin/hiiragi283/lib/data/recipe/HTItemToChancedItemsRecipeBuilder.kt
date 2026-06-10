@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe

class HTItemToChancedItemsRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal val results: MutableList<HTChancedItemResult> = mutableListOf()

    operator fun HTItemResult.unaryPlus() {
        results += this.withChance()
    }

    operator fun HTChancedItemResult.unaryPlus() {
        results += this
    }

    inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        results += HTItemResultBuilder().apply(builderAction).buildWithChanced()
    }

    override fun getPrimalId(): Identifier = results.first().getId()

    override fun createRecipe(): RECIPE = factory.create(ingredient, results, progressData)

    //    Factory    //

    fun interface Factory<out RECIPE> {
        fun create(ingredient: HTItemIngredient, results: List<HTChancedItemResult>, progressData: HTProgressData): RECIPE
    }
}
