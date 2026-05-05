package hiiragi283.core.common.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.base.HTBrewingRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.unwrap
import hiiragi283.core.mixin.PotionBrewingAccessor
import hiiragi283.core.mixin.PotionBrewingMixAccessor
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.brewing.BrewingRecipe

/**
 * バニラのレシピ向けの[HTRecipeType]の実装をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
object HTVanillaRecipeTypes {
    @JvmField
    val SMELTING: HTRecipeType<HCCookingRecipe> = CookingType(RecipeType.SMELTING)

    @JvmField
    val BLASTING: HTRecipeType<HCCookingRecipe> = CookingType(RecipeType.BLASTING)

    @JvmField
    val SMOKING: HTRecipeType<HCCookingRecipe> = CookingType(RecipeType.SMOKING)

    private class CookingType<RECIPE : AbstractCookingRecipe>(private val recipeType: RecipeType<RECIPE>) :
        HTRecipeType<HCCookingRecipe> {
        override fun getId(): ResourceLocation = ResourceLocation.parse(recipeType.toString())

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<HCCookingRecipe>> =
            context.getAllRecipes(recipeType).map { holder: HTRecipeHolder<RECIPE> -> holder.mapRecipe(::HCCookingRecipe) }
    }

    @JvmField
    val BREWING: HTRecipeType<HTBrewingRecipe> = BrewingType

    private data object BrewingType : HTRecipeType<HTBrewingRecipe> {
        private fun getContents(stack: ItemStack): BottledPotionContents? = HTPotionHelper.getContents(stack)

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<HTBrewingRecipe>> {
            val potionBrewing: PotionBrewing = context[HTRecipeLookup.Context.BREWING] ?: return emptySequence()
            // 醸造レシピを登録していく
            return sequence<Either<HCVanillaBrewingRecipe, HCModdedBrewingRecipe>> {
                // Vanilla
                for (accessor: PotionBrewingMixAccessor<Potion> in (potionBrewing as PotionBrewingAccessor).potionMixes) {
                    val recipe = HCVanillaBrewingRecipe(accessor)
                    yield(Either.Left(recipe))
                }
                // Modded
                for (recipe: BrewingRecipe in potionBrewing.recipes.filterIsInstance<BrewingRecipe>()) {
                    val potionFrom: BottledPotionContents = getContents(recipe.input.items[0]) ?: continue
                    val potionTo: BottledPotionContents = getContents(recipe.output) ?: continue
                    yield(Either.Right(HCModdedBrewingRecipe(potionFrom, recipe.ingredient, potionTo)))
                }
            }.mapIndexed { index: Int, recipe: Either<HCVanillaBrewingRecipe, HCModdedBrewingRecipe> ->
                val id: ResourceLocation = recipe.map(
                    {
                        it.potionTo
                            .toLike()
                            .getId()
                            .withSuffix("_$index")
                    },
                    { HiiragiCoreAPI.id("brewing", index.toString()) },
                )
                HTRecipeHolder(id, recipe.unwrap())
            }
        }

        override fun getId(): ResourceLocation = HiiragiCoreAPI.id("brewing")
    }
}
