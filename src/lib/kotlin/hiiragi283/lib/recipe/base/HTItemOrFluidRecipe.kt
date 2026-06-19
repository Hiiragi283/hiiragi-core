package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemAndFluidResult

/**
 * 1種類のアイテムと液体から，1種類のアイテムと液体を作成するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTItemOrFluidRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<HTItemAndFluidResult>,
    HTProgressRecipe<HTItemAndFluidRecipeInput>
