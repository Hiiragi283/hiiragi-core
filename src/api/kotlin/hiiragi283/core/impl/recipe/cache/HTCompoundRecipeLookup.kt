package hiiragi283.core.impl.recipe.cache

import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.TagsUpdatedEvent

class HTCompoundRecipeLookup<RECIPE : Any> private constructor(private val id: ResourceLocation) : HTRecipeLookup<RECIPE> {
    companion object {
        @JvmStatic
        fun <RECIPE : Any> create(id: ResourceLocation): HTCompoundRecipeLookup<RECIPE> = Manager.create(id)
    }

    private val lookups: MutableList<HTRecipeLookup<RECIPE>> = mutableListOf()
    var cachedRecipes: List<HTRecipeHolder<RECIPE>> = listOf()

    internal fun clearCache() {
        cachedRecipes = listOf()
    }

    fun addRecipes(vararg recipes: Pair<ResourceLocation, RECIPE>) {
        addSubLookup { recipes.asSequence().map { (id: ResourceLocation, recipe: RECIPE) -> HTRecipeHolder(id, recipe) } }
    }

    fun addSubLookup(lookup: HTRecipeLookup<RECIPE>) {
        this.lookups += lookup
    }

    override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<RECIPE>> {
        if (cachedRecipes.isEmpty()) {
            cachedRecipes = lookups.flatMap { it.getAllRecipes(context) }
        }
        return cachedRecipes.asSequence()
    }

    override fun toString(): String = "HTCompoundRecipeLookup(id=$id)"

    //    Manager    //

    @EventBusSubscriber
    data object Manager {
        @JvmStatic
        private val instances: MutableMap<ResourceLocation, HTCompoundRecipeLookup<*>> = hashMapOf()

        @JvmStatic
        internal fun <RECIPE : Any> create(id: ResourceLocation): HTCompoundRecipeLookup<RECIPE> {
            val recipeType = HTCompoundRecipeLookup<RECIPE>(id)
            check(instances.put(id, recipeType) == null) { "Duplicated recipe type $id" }
            return recipeType
        }

        @SubscribeEvent
        fun clearCache(event: TagsUpdatedEvent) {
            instances.values.forEach(HTCompoundRecipeLookup<*>::clearCache)
        }
    }
}

//    Extensions    //

fun <INPUT : RecipeInput, RECIPE : Any, R : Recipe<INPUT>> HTCompoundRecipeLookup<RECIPE>.fromRecipeType(
    recipeType: RecipeType<R>,
    transform: (R) -> RECIPE?,
) {
    this.addSubLookup { context: HTRecipeLookup.Context ->
        context
            .getAllRecipes(recipeType)
            .mapNotNull { (id: ResourceLocation, recipe: R) ->
                val recipe1: RECIPE = transform(recipe) ?: return@mapNotNull null
                HTRecipeHolder(id, recipe1)
            }
    }
}
