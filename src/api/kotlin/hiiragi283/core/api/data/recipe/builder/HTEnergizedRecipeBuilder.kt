package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.recipe.base.HTProgressRecipe
import kotlin.properties.Delegates

/**
 * [HTProgressRecipe.Energized]向けの[HTRecipeBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.3
 */
abstract class HTEnergizedRecipeBuilder(prefix: String) : HTRecipeBuilder(prefix) {
    var energy: Int by Delegates.notNull()
}
