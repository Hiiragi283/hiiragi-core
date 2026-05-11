package hiiragi283.lib.recipe.cache

import hiiragi283.lib.recipe.HTRecipeHolder
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import java.util.function.Supplier
import net.minecraft.util.context.ContextMap

@JvmInline
value class HTVanillaRecipeLookup<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(private val recipeType: Supplier<out RecipeType<RECIPE>>) : HTRecipeLookup<RECIPE> {
    override fun getAllRecipes(contextMap: ContextMap): Sequence<HTRecipeHolder<RECIPE>> = contextMap.getOrThrow(HTRecipeLookupContext.RECIPES)
        .byType(recipeType.get())
        .asSequence()
        .map(HTRecipeHolder.Companion::from)
}
