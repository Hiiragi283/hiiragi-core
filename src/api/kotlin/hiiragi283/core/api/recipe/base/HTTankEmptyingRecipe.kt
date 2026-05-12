package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.result.HTItemAndFluidResult

/**
 * 液体入りの容器から，空の容器と液体を取り出すレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
interface HTTankEmptyingRecipe :
    HTRecipePredicates.SingleItem,
    HTRecipeFactories.SingleItemTo<HTItemAndFluidResult>
