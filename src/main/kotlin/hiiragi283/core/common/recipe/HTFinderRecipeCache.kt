package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeFinder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

class HTFinderRecipeCache<INPUT : RecipeInput, RECIPE : Any>(private val finder: HTRecipeFinder<INPUT, RECIPE>) :
    HTRecipeCache<INPUT, RECIPE> {
    private var lastRecipe: Pair<ResourceLocation, RECIPE>? = null

    override fun getFirstRecipe(input: INPUT, level: Level): RECIPE? = finder
        .getRecipeFor(input, level, lastRecipe)
        .also(::lastRecipe::set)
        ?.second
}
