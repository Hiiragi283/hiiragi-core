@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.data.recipe

import hiiragi283.core.api.HCConstants
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.lib.data.recipe.HTRecipeBuilder
import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Ingredient

class HCExplodingRecipeBuilder : HTRecipeBuilder<HCExplodingRecipe>(HCConstants.EXPLODING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HCExplodingRecipeBuilder.() -> Unit): HCExplodingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HCExplodingRecipeBuilder().apply(builderAction)
        }
    }

    var ingredient: Ingredient by HTDelegates.onceInitialize()
    var result: HTChancedItemResult by HTDelegates.onceInitialize()

    override fun getPrimalId(): Identifier = result.getId()

    override fun createRecipe(): HCExplodingRecipe = HCExplodingRecipe(ingredient, result)
}
