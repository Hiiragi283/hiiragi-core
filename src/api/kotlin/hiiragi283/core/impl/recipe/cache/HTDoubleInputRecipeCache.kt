package hiiragi283.core.impl.recipe.cache

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.recipe
import net.minecraft.world.level.Level
import java.util.function.BiPredicate
import net.minecraft.resources.ResourceLocation

/**
 * @see mekanism.api.recipes.cache.TwoInputCachedRecipe
 */
abstract class HTDoubleInputRecipeCache<INPUT_A : Any, INPUT_B : Any, RECIPE : BiPredicate<INPUT_A, INPUT_B>>(
    lookup: HTRecipeLookup<RECIPE>,
) : HTBasicRecipeCache<RECIPE>(lookup) {
    fun findFirstRecipe(firstInput: INPUT_A, secondInput: INPUT_B, level: Level): RECIPE? = findFirstRecipe(firstInput, secondInput, HTRecipeLookup.Context.create(level))

    fun findFirstRecipe(firstInput: INPUT_A, secondInput: INPUT_B, context: HTRecipeLookup.Context): RECIPE? = findFirstHolder(firstInput, secondInput, context)?.recipe

    fun findFirstHolder(firstInput: INPUT_A, secondInput: INPUT_B, context: HTRecipeLookup.Context): HTRecipeHolder<RECIPE>? {
        if (isEmpty(firstInput, secondInput)) return null
        if (lastRecipe != null && lastRecipe!!.recipe.test(firstInput, secondInput)) {
            return lastRecipe
        }
        for ((id: ResourceLocation, recipe: RECIPE) in lookup.getAllRecipes(context)) {
            if (recipe.test(firstInput, secondInput)) {
                lastRecipe = id to recipe
                break
            }
        }
        return lastRecipe
    }

    protected abstract fun isEmpty(firstInput: INPUT_A, secondInput: INPUT_B): Boolean
}
