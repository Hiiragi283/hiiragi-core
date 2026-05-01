package hiiragi283.core.impl.recipe.cache

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.TriPredicate

abstract class HTTripleInputRecipeCache<INPUT_A : Any, INPUT_B : Any, INPUT_C : Any, RECIPE : TriPredicate<INPUT_A, INPUT_B, INPUT_C>>(
    lookup: HTRecipeLookup<RECIPE>,
) : HTBasicRecipeCache<RECIPE>(lookup) {
    fun findFirstRecipe(
        firstInput: INPUT_A,
        secondInput: INPUT_B,
        thirdInput: INPUT_C,
        level: Level,
    ): RECIPE? = findFirstRecipe(firstInput, secondInput, thirdInput, HTRecipeLookup.Context.create(level))

    fun findFirstRecipe(
        firstInput: INPUT_A,
        secondInput: INPUT_B,
        thirdInput: INPUT_C,
        context: HTRecipeLookup.Context,
    ): RECIPE? = findFirstHolder(firstInput, secondInput, thirdInput, context)?.recipe

    fun findFirstHolder(
        firstInput: INPUT_A,
        secondInput: INPUT_B,
        thirdInput: INPUT_C,
        context: HTRecipeLookup.Context,
    ): HTRecipeHolder<RECIPE>? {
        if (isEmpty(firstInput, secondInput, thirdInput)) return null
        if (lastRecipe != null && lastRecipe!!.recipe.test(firstInput, secondInput, thirdInput)) {
            return lastRecipe
        }
        lastRecipe = lookup
            .getAllRecipes(context)
            .firstOrNull { (_, recipe: RECIPE) -> recipe.test(firstInput, secondInput, thirdInput) }
            ?: return null
        return lastRecipe
    }

    protected abstract fun isEmpty(firstInput: INPUT_A, secondInput: INPUT_B, thirdInput: INPUT_C): Boolean
}
