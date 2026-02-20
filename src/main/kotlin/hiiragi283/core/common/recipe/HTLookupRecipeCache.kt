package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.toPair
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

class HTLookupRecipeCache<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(val lookup: HTRecipeLookup<INPUT, RECIPE>) :
    HTRecipeCache<INPUT, RECIPE> {
    private var lastRecipe: Pair<ResourceLocation, RECIPE>? = null

    override fun getFirstRecipe(input: INPUT, level: Level): RECIPE? {
        val (_, recipe: RECIPE) = lastRecipe ?: return run {
            lookup
                .findFirst(level) { it.matches(input, level) }
                .also { holder: RecipeHolder<RECIPE>? -> lastRecipe = holder?.toPair() }
                ?.value()
        }
        if (recipe.matches(input, level)) {
            return recipe
        } else {
            lastRecipe = null
            return null
        }
    }
}
