package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.recipe.base.HTProgressRecipe

/**
 * [HTProgressRecipe.Ticking]向けの[HTRecipeBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.3
 */
abstract class HTTickingRecipeBuilder(prefix: String) : HTRecipeBuilder(prefix) {
    var time: Int = 20 * 10
}
