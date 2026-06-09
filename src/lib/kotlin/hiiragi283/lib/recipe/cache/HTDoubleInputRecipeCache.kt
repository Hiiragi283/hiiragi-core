package hiiragi283.lib.recipe.cache

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookupContext
import java.util.function.BiPredicate
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.context.ContextMap

/**
 * @see mekanism.api.recipes.cache.TwoInputCachedRecipe
 */
abstract class HTDoubleInputRecipeCache<INPUT_A : Any, INPUT_B : Any, RECIPE : BiPredicate<INPUT_A, INPUT_B>>(lookup: HTRecipeLookup<RECIPE>) : HTBasicRecipeCache<RECIPE>(lookup) {
    fun findFirstRecipe(firstInput: INPUT_A, secondInput: INPUT_B, level: ServerLevel): RECIPE? = findFirstRecipe(firstInput, secondInput, HTRecipeLookupContext.create(level))

    fun findFirstRecipe(firstInput: INPUT_A, secondInput: INPUT_B, context: ContextMap): RECIPE? = findFirstHolder(firstInput, secondInput, context)?.recipe

    fun findFirstHolder(firstInput: INPUT_A, secondInput: INPUT_B, context: ContextMap): HTRecipeHolder<RECIPE>? {
        if (isEmpty(firstInput, secondInput)) return null
        if (lastRecipe != null && lastRecipe!!.recipe.test(firstInput, secondInput)) {
            return lastRecipe
        }
        lastRecipe = lookup
            .getAllRecipes(context)
            .firstOrNull { (_, recipe: RECIPE) -> recipe.test(firstInput, secondInput) }
            ?: return null
        return lastRecipe
    }

    protected abstract fun isEmpty(firstInput: INPUT_A, secondInput: INPUT_B): Boolean
}
