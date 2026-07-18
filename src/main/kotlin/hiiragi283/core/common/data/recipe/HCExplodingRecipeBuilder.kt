@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.common.recipe.HCExplodingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class HCExplodingRecipeBuilder : HTRecipeBuilder<HCExplodingRecipe>(HTConst.EXPLODING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HCExplodingRecipeBuilder.() -> Unit): HCExplodingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HCExplodingRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal var ingredient: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var result: HTChancedItemResult by HTDelegates.onceInitialize()

    operator fun Ingredient.unaryPlus() {
        ingredient = this
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

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HCExplodingRecipe = HCExplodingRecipe(ingredient, result)
}
