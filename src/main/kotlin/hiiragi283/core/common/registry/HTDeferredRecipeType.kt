package hiiragi283.core.common.registry

import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

class HTDeferredRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> :
    HTBasicHolderLike<RecipeType<*>, RecipeType<RECIPE>>,
    HTRecipeType.Managed<INPUT, RECIPE> {
    constructor(key: ResourceKey<RecipeType<*>>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.RECIPE_TYPE, id)

    @Suppress("UNCHECKED_CAST")
    override fun get(): RecipeType<RECIPE> = BuiltInRegistries.RECIPE_TYPE.getOrThrow(key) as RecipeType<RECIPE>

    override fun createCache(): HTRecipeCache<INPUT, RECIPE> = HTLookupRecipeCache.forManager(this)

    override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<RecipeHolder<RECIPE>> = context.getAllRecipes(get())

    override fun toString(): String = "HTDeferredRecipeType(key=$key)"
}
