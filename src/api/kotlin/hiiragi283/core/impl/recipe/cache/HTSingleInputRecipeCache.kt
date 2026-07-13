package hiiragi283.core.impl.recipe.cache

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.recipe
import net.minecraft.world.level.Level
import java.util.function.Predicate
import net.minecraft.resources.ResourceLocation

/**
 * @see mekanism.common.recipe.lookup.cache.SingleInputRecipeCache
 */
abstract class HTSingleInputRecipeCache<INPUT : Any, RECIPE : Predicate<INPUT>>(lookup: HTRecipeLookup<RECIPE>) : HTBasicRecipeCache<RECIPE>(lookup) {
    fun findFirstRecipe(input: INPUT, level: Level): RECIPE? = findFirstRecipe(input, HTRecipeLookup.Context.create(level))

    fun findFirstRecipe(input: INPUT, context: HTRecipeLookup.Context): RECIPE? = findFirstHolder(input, context)?.recipe

    fun findFirstHolder(input: INPUT, context: HTRecipeLookup.Context): HTRecipeHolder<RECIPE>? {
        if (isEmpty(input)) return null
        if (lastRecipe != null && lastRecipe!!.recipe.test(input)) {
            return lastRecipe
        }
        for ((id: ResourceLocation, recipe: RECIPE) in lookup.getAllRecipes(context)) {
            if (recipe.test(input)) {
                lastRecipe = id to recipe
                break
            }
        }
        return lastRecipe
    }

    protected abstract fun isEmpty(input: INPUT): Boolean
}
