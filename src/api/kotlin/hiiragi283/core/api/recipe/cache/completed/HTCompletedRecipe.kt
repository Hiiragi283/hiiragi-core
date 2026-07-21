package hiiragi283.core.api.recipe.cache.completed

import hiiragi283.core.api.recipe.base.HTProgressData

/**
 * @see mekanism.api.recipes.cache.CachedRecipe
 */
abstract class HTCompletedRecipe<RECIPE : Any>(val recipe: RECIPE) {
    abstract fun canComplete(): Boolean

    abstract fun complete()

    abstract class WithProgress<RECIPE : Any>(recipe: RECIPE) : HTCompletedRecipe<RECIPE>(recipe) {
        abstract fun getProgress(): HTProgressData
    }
}
