package hiiragi283.core.support.recipe.cache

import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import java.util.function.Supplier
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

@JvmInline
value class HTVanillaRecipeLookup<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(private val recipeType: Supplier<out RecipeType<RECIPE>>) : HTRecipeLookup<RECIPE> {
    constructor(recipeType: RecipeType<RECIPE>) : this({ recipeType })

    override fun getAllRecipes(context: HTRecipeLookup.Context): Map<ResourceLocation, RECIPE> {
        val map: MutableMap<ResourceLocation, RECIPE> = mutableMapOf()
        for ((first: ResourceLocation, second: RECIPE) in context.getAllRecipes(recipeType.get())) {
            if (!second.isIncomplete) map[first] = second
        }
        return map
    }

    override fun toString(): String = "HTVanillaRecipeLookup(recipeType=${recipeType.get()})"
}
