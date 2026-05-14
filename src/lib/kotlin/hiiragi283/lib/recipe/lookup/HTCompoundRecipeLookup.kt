package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.recipe.HTRecipeHolder
import net.minecraft.resources.Identifier
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.TagsUpdatedEvent

class HTCompoundRecipeLookup<RECIPE : Any> private constructor(private val id: Identifier) : HTRecipeLookup<RECIPE> {
    companion object {
        @JvmStatic
        fun <RECIPE : Any> create(id: Identifier): HTCompoundRecipeLookup<RECIPE> = Manager.create(id)
    }

    private val lookups: MutableList<HTRecipeLookup<RECIPE>> = mutableListOf()
    var cachedRecipes: List<HTRecipeHolder<RECIPE>> = listOf()

    internal fun clearCache() {
        cachedRecipes = listOf()
    }

    fun addRecipes(vararg recipes: Pair<Identifier, RECIPE>) {
        addSubLookup { recipes.asSequence().map { (id: Identifier, recipe: RECIPE) -> HTRecipeHolder(id, recipe) } }
    }

    fun addSubLookup(lookup: HTRecipeLookup<RECIPE>) {
        this.lookups += lookup
    }

    override fun getAllRecipes(contextMap: ContextMap): Sequence<HTRecipeHolder<RECIPE>> {
        if (cachedRecipes.isEmpty()) {
            cachedRecipes = lookups.flatMap { it.getAllRecipes(contextMap) }
        }
        return cachedRecipes.asSequence()
    }

    override fun toString(): String = "HTCompoundRecipeLookup(id=$id)"

    //    Manager    //

    @EventBusSubscriber
    data object Manager {
        @JvmStatic
        private val instances: MutableMap<Identifier, HTCompoundRecipeLookup<*>> = hashMapOf()

        @JvmStatic
        internal fun <RECIPE : Any> create(id: Identifier): HTCompoundRecipeLookup<RECIPE> {
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
    this.addSubLookup { contextMap: ContextMap ->
        contextMap.getOrThrow(HTRecipeLookupContext.RECIPES)
            .byType(recipeType)
            .asSequence()
            .mapNotNull { holder: RecipeHolder<R> -> holder.value().let(transform)?.let { HTRecipeHolder(holder.id(), it) } }
    }
}
