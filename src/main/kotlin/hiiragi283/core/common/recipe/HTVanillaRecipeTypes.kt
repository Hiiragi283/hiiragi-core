package hiiragi283.core.common.recipe

import com.google.common.collect.ImmutableMultimap
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.registry.toLike
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeType

/**
 * バニラのレシピ向けの[HTRecipeType]の実装をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
data object HTVanillaRecipeTypes {
    @JvmField
    val SMELTING: HTRecipeType<HCCookingRecipe> = CookingType(RecipeType.SMELTING)

    @JvmField
    val BLASTING: HTRecipeType<HCCookingRecipe> = CookingType(RecipeType.BLASTING)

    @JvmField
    val SMOKING: HTRecipeType<HCCookingRecipe> = CookingType(RecipeType.SMOKING)

    @JvmInline
    private value class CookingType<RECIPE : AbstractCookingRecipe>(private val recipeType: RecipeType<RECIPE>) : HTRecipeType<HCCookingRecipe> {
        override fun getId(): ResourceLocation = ResourceLocation.parse(recipeType.toString())

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<HCCookingRecipe>> = context.getAllRecipes(recipeType).map { holder: HTRecipeHolder<RECIPE> -> holder.mapRecipe(::HCCookingRecipe) }
    }

    @JvmField
    val BREWING: HTRecipeType<HCBrewingRecipe> = BrewingType

    private data object BrewingType : HTRecipeType<HCBrewingRecipe> {
        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<HCBrewingRecipe>> {
            val builder: ImmutableMultimap.Builder<Holder<Potion>, HCBrewingRecipe> = ImmutableMultimap.builder()
            context[HTRecipeLookup.Context.BREWING]
                ?.potionMixes
                ?.map(::HCBrewingRecipe)
                ?.forEach { builder.put(it.potionTo, it) }
            val recipeMap: ImmutableMultimap<Holder<Potion>, HCBrewingRecipe> = builder.build()
            return recipeMap
                .keySet()
                .asSequence()
                .flatMap { potionTo: Holder<Potion> ->
                    recipeMap[potionTo].mapIndexed { index: Int, recipe: HCBrewingRecipe ->
                        HTRecipeHolder(potionTo.toLike().getId().withSuffix("_$index"), recipe)
                    }
                }
        }

        override fun getId(): ResourceLocation = HiiragiCoreAPI.id("brewing")
    }
}
