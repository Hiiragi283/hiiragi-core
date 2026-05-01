package hiiragi283.core.impl.recipe.cache

/**
 * @see mekanism.api.recipes.cache.CachedRecipe
 */
abstract class HTCompletedRecipe<RECIPE : Any>(val recipe: RECIPE) {
    abstract fun canComplete(): Boolean

    abstract fun complete()
}
