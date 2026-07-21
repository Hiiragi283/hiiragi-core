package hiiragi283.core.common.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.MultiMap
import hiiragi283.core.api.collection.buildListMultiMap
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.registry.toLike
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeType

/**
 * バニラのレシピ向けの[HTRecipeLookup]の実装をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
data object VanillaRecipeLookups {
    @JvmField
    val SMELTING: HTRecipeLookup.Translatable<HCCookingRecipe> = CookingLookup(RecipeType.SMELTING)

    @JvmField
    val BLASTING: HTRecipeLookup.Translatable<HCCookingRecipe> = CookingLookup(RecipeType.BLASTING)

    @JvmField
    val SMOKING: HTRecipeLookup.Translatable<HCCookingRecipe> = CookingLookup(RecipeType.SMOKING)

    @JvmRecord
    private data class CookingLookup<RECIPE : AbstractCookingRecipe>(private val recipeType: RecipeType<RECIPE>) : HTRecipeLookup.Translatable<HCCookingRecipe> {
        override fun getAllRecipes(context: HTRecipeLookup.Context): Map<ResourceLocation, HCCookingRecipe> = HiiragiCoreAccess.INSTANCE
            .getAllRecipes(context, recipeType)
            .mapValues { (_, recipe: RECIPE) -> HCCookingRecipe(recipe) }

        override fun getKey(): ResourceKey<RecipeType<*>> = BuiltInRegistries.RECIPE_TYPE.wrapAsHolder(recipeType).getKeyOrThrow()
    }

    @JvmField
    val BREWING: HTRecipeLookup.Translatable<HCBrewingRecipe> = BrewingLookup

    private data object BrewingLookup : HTRecipeLookup.Translatable<HCBrewingRecipe> {
        override fun getAllRecipes(context: HTRecipeLookup.Context): Map<ResourceLocation, HCBrewingRecipe> {
            val multiMap: MultiMap<Holder<Potion>, HCBrewingRecipe> = buildListMultiMap {
                context[HTRecipeLookup.Context.BREWING]
                    ?.let(PotionBrewing::potionMixes)
                    ?.asSequence()
                    ?.map(::HCBrewingRecipe)
                    ?.filterNot(HCBrewingRecipe::isIncomplete)
                    ?.forEach { put(it.potionTo, it) }
            }
            if (multiMap.isEmpty) return mapOf()
            return multiMap.keys.flatMap { potionTo: Holder<Potion> ->
                multiMap[potionTo].mapIndexed { index: Int, recipe: HCBrewingRecipe ->
                    HTRecipeHolder(potionTo.toLike().getId().withSuffix("_$index"), recipe)
                }
            }.toMap()
        }

        override fun getKey(): ResourceKey<RecipeType<*>> = Registries.RECIPE_TYPE.createKey(HiiragiCoreAPI.id("brewing"))
    }
}
