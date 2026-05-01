package hiiragi283.core.impl.recipe.cache

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.cache.HTRecipeLookup

/**
 * @see mekanism.common.recipe.lookup.cache.AbstractInputRecipeCache
 */
abstract class HTBasicRecipeCache<RECIPE : Any>(protected val lookup: HTRecipeLookup<RECIPE>) {
    protected var lastRecipe: HTRecipeHolder<RECIPE>? = null
}
