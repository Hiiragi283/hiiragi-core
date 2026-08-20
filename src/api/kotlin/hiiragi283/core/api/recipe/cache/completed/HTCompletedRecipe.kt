package hiiragi283.core.api.recipe.cache.completed

import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import net.minecraft.world.item.crafting.RecipeInput

/**
 * @see mekanism.api.recipes.cache.CachedRecipe
 */
abstract class HTCompletedRecipe<RECIPE : Any>(val recipe: RECIPE) {
    abstract fun canComplete(): Boolean

    abstract fun complete()

    abstract class WithProgress<INPUT : RecipeInput, RECIPE : HTProgressRecipe<INPUT>>(recipe: RECIPE) : HTCompletedRecipe<RECIPE>(recipe) {
        protected val input: INPUT by lazy { createInput() }

        fun getProgress(): HTProgressData = recipe.getProgressData(input)

        protected abstract fun createInput(): INPUT
    }
}
