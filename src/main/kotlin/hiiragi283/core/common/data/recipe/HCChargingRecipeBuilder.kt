@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.common.recipe.HCChargingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation

class HCChargingRecipeBuilder : HTRecipeBuilder<HCChargingRecipe>(HTConst.CHARGING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HCChargingRecipeBuilder.() -> Unit): HCChargingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HCChargingRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var result: HTChancedItemResult by HTDelegates.onceInitialize()
    var energy: Int = HCChargingRecipe.DEFAULT_ENERGY

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    operator fun HTItemResult.unaryPlus() {
        result = this.withChance()
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = HTItemResultBuilder().apply(builderAction).buildWithChanced()
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HCChargingRecipe = HCChargingRecipe(ingredient, result, energy)
}
