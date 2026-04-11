package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

class HTRecipeTypeImpl<INPUT : RecipeInput, RECIPE : Any>(private val id: ResourceLocation) : HTRecipeType<INPUT, RECIPE> {
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

    fun addProvider(other: HTRecipeTypeImpl<INPUT, RECIPE>) {
        this.addProvider(other::getAllRecipes)
    }

    fun addProvider(provider: Provider<HTRecipeHolder<RECIPE>>) {
        this.providers += provider
    }

    override fun getId(): ResourceLocation = id

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

fun <INPUT : RecipeInput, RECIPE : Any, R : Recipe<INPUT>> HTRecipeTypeImpl<INPUT, RECIPE>.addProvider(
    recipeType: RecipeType<R>,
    transform: (R) -> RECIPE,
) {
    this.addProvider { context: HTRecipeLookup.Context ->
        context
            .getAllRecipes(recipeType)
            .map { holder -> holder.mapRecipe(transform) }
    }
}
