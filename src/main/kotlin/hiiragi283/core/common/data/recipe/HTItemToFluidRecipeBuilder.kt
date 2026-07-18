@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.data.recipe.HTFluidResultBuilder
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.impl.data.recipe.HTItemToResultRecipeBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.world.item.crafting.Recipe

class HTItemToFluidRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, factory: Factory<HTFluidResult, RECIPE>) : HTItemToResultRecipeBuilder<RECIPE, HTFluidResult>(prefix, factory) {
    fun result(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = HTFluidResultBuilder().apply(builderAction).build()
    }
}
