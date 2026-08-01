package hiiragi283.core.common.recipe

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.registry.getKeyOrThrow
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
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
}
