@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.data.recipe

import hiiragi283.core.api.HCConstants
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.lib.data.recipe.HTItemResultBuilder
import hiiragi283.lib.data.recipe.HTRecipeBuilder
import hiiragi283.lib.data.recipe.IngredientBuilder
import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Ingredient

class HCChargingRecipeBuilder : HTRecipeBuilder<HCChargingRecipe>(HCConstants.CHARGING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HCChargingRecipeBuilder.() -> Unit): HCChargingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HCChargingRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal var ingredient: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var result: HTChancedItemResult by HTDelegates.onceInitialize()
    var energy: Int = HCChargingRecipe.DEFAULT_ENERGY

    operator fun Ingredient.unaryPlus() {
        ingredient = this
    }

    operator fun HTItemResult.unaryPlus() {
        result = this.withChance()
    }

    operator fun HTChancedItemResult.unaryPlus() {
        result = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder().apply(builderAction).build()
    }

    inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = HTItemResultBuilder().apply(builderAction).buildWithChanced()
    }

    override fun getPrimalId(): Identifier = result.getId()

    override fun createRecipe(): HCChargingRecipe = HCChargingRecipe(ingredient, result, energy)
}
