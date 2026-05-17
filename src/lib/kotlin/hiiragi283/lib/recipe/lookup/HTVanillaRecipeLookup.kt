package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.recipe.HTRecipeHolder
import java.util.function.Supplier
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

@JvmInline
value class HTVanillaRecipeLookup<INPUT : RecipeInput, out RECIPE : Recipe<INPUT>>(private val recipeType: Supplier<out RecipeType<RECIPE>>) : HTRecipeLookup<RECIPE> {
    override fun getAllRecipes(contextMap: ContextMap): Sequence<HTRecipeHolder<RECIPE>> = contextMap.getOrThrow(HTRecipeLookupContext.RECIPES)
        .byType(recipeType.get())
        .asSequence()
        .map(HTRecipeHolder.Companion::from)
}
