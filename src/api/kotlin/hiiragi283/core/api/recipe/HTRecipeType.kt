package hiiragi283.core.api.recipe

import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.resource.HTKeyLike
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

@JvmRecord
data class HTRecipeType<T : Recipe<*>>(private val id: ResourceLocation) :
    RecipeType<T>,
    HTKeyLike.SimpleTranslatable<RecipeType<*>> {
    override fun getKey(): ResourceKey<RecipeType<*>> = Registries.RECIPE_TYPE.createKey(id)
}
