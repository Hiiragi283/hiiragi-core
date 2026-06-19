package hiiragi283.lib.recipe.cache

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.lookup.HTRecipeLookup

/**
 * レシピキャッシュの基礎となるクラスです。
 *
 * 参照 : [Mekanism - AbstractInputRecipeCache](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/recipe/lookup/cache/AbstractInputRecipeCache.java)
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTBasicRecipeCache<out RECIPE>(protected val lookup: HTRecipeLookup<RECIPE>) {
    protected var lastRecipe: HTRecipeHolder<@UnsafeVariance RECIPE>? = null
}
