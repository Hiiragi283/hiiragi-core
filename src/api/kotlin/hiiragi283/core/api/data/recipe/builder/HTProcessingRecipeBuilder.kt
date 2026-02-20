package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.recipe.HTProcessingRecipe

/**
 * [HTProcessingRecipe]向けの[HTRecipeBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
abstract class HTProcessingRecipeBuilder(prefix: String) : HTRecipeBuilder(prefix) {
    var time: Int = 20 * 10
        set(value) {
            require(value > 0) { "Recipe time must be positive" }
            field = value
        }
}
