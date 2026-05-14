package hiiragi283.lib.recipe.cache

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.lookup.HTRecipeLookup

/**
 * @see mekanism.common.recipe.lookup.cache.AbstractInputRecipeCache
 */
abstract class HTBasicRecipeCache<RECIPE : Any>(protected val lookup: HTRecipeLookup<RECIPE>) {
    protected var lastRecipe: HTRecipeHolder<RECIPE>? = null
}
