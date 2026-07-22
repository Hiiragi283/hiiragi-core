@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.support.data.recipe

import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTProgressRecipeBuilder
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

abstract class HTMultiOutputRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    protected val results: List<HTChancedItemResult> field: MutableList<HTChancedItemResult> = mutableListOf()

    operator fun HTItemResult.unaryPlus() {
        +this.withChance()
    }

    operator fun HTChancedItemResult.unaryPlus() {
        results += this
    }

    fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        results += HTItemResultBuilder().apply(builderAction).buildWithChanced()
    }

    override fun getPrimalId(): ResourceLocation = results.first().getId()
}
