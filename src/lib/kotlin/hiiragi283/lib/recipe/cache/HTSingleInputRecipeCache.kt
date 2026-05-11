package hiiragi283.lib.recipe.cache

import hiiragi283.lib.recipe.HTRecipeHolder
import java.util.function.Predicate
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.context.ContextMap

/**
 * @see mekanism.common.recipe.lookup.cache.SingleInputRecipeCache
 */
abstract class HTSingleInputRecipeCache<INPUT : Any, RECIPE : Predicate<INPUT>>(lookup: HTRecipeLookup<RECIPE>) : HTBasicRecipeCache<RECIPE>(lookup) {
    fun findFirstRecipe(input: INPUT, level: ServerLevel): RECIPE? = findFirstRecipe(input, HTRecipeLookupContext.create(level))

    fun findFirstRecipe(input: INPUT, context: ContextMap): RECIPE? = findFirstHolder(input, context)?.recipe

    fun findFirstHolder(input: INPUT, context: ContextMap): HTRecipeHolder<RECIPE>? {
        if (isEmpty(input)) return null
        if (lastRecipe != null && lastRecipe!!.recipe.test(input)) {
            return lastRecipe
        }
        lastRecipe = lookup
            .getAllRecipes(context)
            .firstOrNull { (_, recipe: RECIPE) -> recipe.test(input) }
            ?: return null
        return lastRecipe
    }

    protected abstract fun isEmpty(input: INPUT): Boolean
}
