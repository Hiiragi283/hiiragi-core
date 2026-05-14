package hiiragi283.core.api.recipe

import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import hiiragi283.lib.recipe.result.HTItemAndFluidResult

/**
 * 液体入りの容器から，空の容器と液体を取り出すレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
interface HTTankEmptyingRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<HTItemAndFluidResult>
