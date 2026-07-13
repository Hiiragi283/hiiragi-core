package hiiragi283.core.impl.recipe.cache

import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import java.util.function.Supplier
import net.minecraft.resources.ResourceLocation

@JvmInline
value class HTVanillaRecipeLookup<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(private val recipeType: Supplier<out RecipeType<RECIPE>>) : HTRecipeLookup<RECIPE> {
    override fun getAllRecipes(context: HTRecipeLookup.Context): Map<ResourceLocation, RECIPE> = context.getAllRecipes(recipeType.get()).toMap()

    override fun toString(): String = "HTVanillaRecipeLookup(recipeType=${recipeType.get()})"
}
