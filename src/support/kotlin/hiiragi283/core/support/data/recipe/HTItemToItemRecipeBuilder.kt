@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.support.data.recipe

import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.recipe.result.HTItemResult
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.world.item.crafting.Recipe

class HTItemToItemRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, factory: Factory<HTItemResult, RECIPE>) : HTItemToResultRecipeBuilder<RECIPE, HTItemResult>(prefix, factory) {
    fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = HTItemResultBuilder().apply(builderAction).build()
    }
}
