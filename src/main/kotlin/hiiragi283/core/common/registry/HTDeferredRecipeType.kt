package hiiragi283.core.common.registry

import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.common.recipe.HTLookupRecipeCache
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

data class HTDeferredRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(private val key: ResourceKey<RecipeType<*>>) :
    HTHolderLike<RecipeType<*>, RecipeType<RECIPE>>,
    HTRecipeType<INPUT, RECIPE> {
    constructor(id: ResourceLocation) : this(Registries.RECIPE_TYPE.createKey(id))

    override fun getResourceKey(): ResourceKey<RecipeType<*>> = key

    @Suppress("UNCHECKED_CAST")
    override fun get(): RecipeType<RECIPE> = BuiltInRegistries.RECIPE_TYPE.getOrThrow(key) as RecipeType<RECIPE>

    override fun createCache(): HTRecipeCache<INPUT, RECIPE> = HTLookupRecipeCache(this)

    override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<RecipeHolder<RECIPE>> = context.getAllRecipes(get())
}
