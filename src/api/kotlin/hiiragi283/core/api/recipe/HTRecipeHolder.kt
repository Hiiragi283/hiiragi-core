package hiiragi283.core.api.recipe

import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

@JvmRecord
data class HTRecipeHolder<RECIPE : Any>(
    @JvmField val id: ResourceLocation,
    @JvmField val recipe: RECIPE,
) : HTIdLike {
    companion object {
        @JvmStatic
        fun <RECIPE : Recipe<*>> from(holder: RecipeHolder<RECIPE>): HTRecipeHolder<RECIPE> = HTRecipeHolder(holder.id(), holder.value())
    }

    constructor(pair: Pair<ResourceLocation, RECIPE>) : this(pair.first, pair.second)

    constructor(entry: Map.Entry<ResourceLocation, RECIPE>) : this(entry.key, entry.value)

    inline fun <R : Any> mapRecipe(transform: (RECIPE) -> R): HTRecipeHolder<R> = HTRecipeHolder(this.id, transform(this.recipe))

    override fun getId(): ResourceLocation = id
}

fun <RECIPE : Recipe<*>> HTRecipeHolder<RECIPE>.toVanilla(): RecipeHolder<RECIPE> = RecipeHolder(this.id, this.recipe)
