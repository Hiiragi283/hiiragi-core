package hiiragi283.core.impl.recipe.cache

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

data class HTRecipeLookupImpl<RECIPE : Any>(private val id: ResourceLocation) : HTRecipeLookup<RECIPE> {
    private val providers: MutableList<Provider<HTRecipeHolder<RECIPE>>> = mutableListOf()
    var cachedRecipes: List<HTRecipeHolder<RECIPE>> = listOf()

    internal fun clearCache() {
        cachedRecipes = listOf()
    }

    fun addProvider(recipes: Iterable<HTRecipeHolder<RECIPE>>) {
        this.addProvider { recipes.asSequence() }
    }

    fun addProvider(vararg recipes: Pair<ResourceLocation, RECIPE>) {
        this.addProvider { recipes.asSequence().map(::HTRecipeHolder) }
    }

    fun addProvider(other: HTRecipeLookupImpl<RECIPE>) {
        this.addProvider(other::getAllRecipes)
    }

    fun addProvider(provider: Provider<HTRecipeHolder<RECIPE>>) {
        this.providers += provider
    }

    override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<RECIPE>> {
        if (cachedRecipes.isEmpty()) {
            cachedRecipes = providers.flatMap { it.getAllRecipes(context) }
        }
        return cachedRecipes.asSequence()
    }

    fun interface Provider<HOLDER : Any> {
        fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<HOLDER>
    }
}

//    Extensions    //

fun <INPUT : RecipeInput, RECIPE : Any, R : Recipe<INPUT>> HTRecipeLookupImpl<RECIPE>.addProvider(
    recipeType: RecipeType<R>,
    transform: (R) -> RECIPE,
) {
    this.addProvider { context: HTRecipeLookup.Context ->
        context
            .getAllRecipes(recipeType)
            .map { holder -> holder.mapRecipe(transform) }
    }
}
