package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

class HTLookupRecipeCache<INPUT : RecipeInput, RECIPE : Any, HOLDER : Any>(
    val lookup: HTRecipeLookup<INPUT, RECIPE, HOLDER>,
    private val predicate: (RECIPE, INPUT, Level) -> Boolean,
) : HTRecipeCache<INPUT, RECIPE> {
    companion object {
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> forManager(
            lookup: HTRecipeLookup.Managed<INPUT, RECIPE>,
        ): HTLookupRecipeCache<INPUT, RECIPE, RecipeHolder<RECIPE>> = HTLookupRecipeCache(lookup, Recipe<INPUT>::matches)

        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>, HOLDER : Any> forRecipe(
            lookup: HTRecipeLookup<INPUT, RECIPE, HOLDER>,
        ): HTLookupRecipeCache<INPUT, RECIPE, HOLDER> =
            HTLookupRecipeCache(lookup) { recipe: RECIPE, input: INPUT, _ -> recipe.test(input) }
    }

    private var lastRecipe: HOLDER? = null

    override fun getFirstRecipe(input: INPUT, level: Level): RECIPE? {
        val holder: HOLDER = lastRecipe ?: return run {
            lookup.findFirst(level) { predicate(it, input, level) }.also(::lastRecipe::set)?.let(lookup::getRecipe)
        }
        val recipe: RECIPE = lookup.getRecipe(holder)
        if (predicate(recipe, input, level)) {
            return recipe
        } else {
            lastRecipe = null
            return null
        }
    }
}
