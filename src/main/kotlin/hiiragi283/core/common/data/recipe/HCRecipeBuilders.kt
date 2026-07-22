@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.support.data.recipe.HTItemToMultiItemRecipeBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

data object HCRecipeBuilders {
    @JvmStatic
    inline fun crushing(builderAction: HTItemToMultiItemRecipeBuilder<HCCrushingRecipe>.() -> Unit): HTItemToMultiItemRecipeBuilder<HCCrushingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToMultiItemRecipeBuilder(HTConst.CRUSHING, ::HCCrushingRecipe).apply {
            time /= 2
            builderAction()
        }
    }
}
