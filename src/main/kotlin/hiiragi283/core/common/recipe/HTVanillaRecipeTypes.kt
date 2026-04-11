package hiiragi283.core.common.recipe

import com.google.common.collect.ImmutableMultimap
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.data.recipe.HTResultCreator
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.registry.toHolderSet
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.common.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.core.mixin.PotionBrewingAccessor
import hiiragi283.core.mixin.PotionBrewingMixAccessor
import hiiragi283.core.util.HCPotionFluidHelper
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.common.brewing.BrewingRecipe

/**
 * バニラのレシピ向けの[HTRecipeType]の実装をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
object HTVanillaRecipeTypes {
    @JvmField
    val SMELTING: HTRecipeType<SingleRecipeInput, HCCookingRecipe> = CookingType(RecipeType.SMELTING)

    @JvmField
    val BLASTING: HTRecipeType<SingleRecipeInput, HCCookingRecipe> = CookingType(RecipeType.BLASTING)

    @JvmField
    val SMOKING: HTRecipeType<SingleRecipeInput, HCCookingRecipe> = CookingType(RecipeType.SMOKING)

    private class CookingType<RECIPE : AbstractCookingRecipe>(private val recipeType: RecipeType<RECIPE>) :
        HTRecipeType<SingleRecipeInput, HCCookingRecipe> {
        override fun getId(): ResourceLocation = ResourceLocation.parse(recipeType.toString())

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<HCCookingRecipe>> =
            context.getAllRecipes(recipeType).map { holder: HTRecipeHolder<RECIPE> -> holder.mapRecipe(::HCCookingRecipe) }
    }

    @JvmField
    val BREWING: HTRecipeType<HTItemAndFluidRecipeInput, HCBrewingRecipe> = BrewingType

    private data object BrewingType : HTRecipeType<HTItemAndFluidRecipeInput, HCBrewingRecipe> {
        private var storedBrewing: PotionBrewing = PotionBrewing.EMPTY
        private var cachedRecipes: Sequence<HTRecipeHolder<HCBrewingRecipe>> = emptySequence()

        private fun getPotion(stack: ItemStack): Holder<Potion> = HTPotionHelper.getPotion(stack).potion.orElseGet(Potions::WATER)

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<HTRecipeHolder<HCBrewingRecipe>> {
            // すでにレシピが生成されている場合はパス
            val potionBrewing: PotionBrewing = context.brewing
            if (potionBrewing == storedBrewing && cachedRecipes.any()) {
                return cachedRecipes
            }
            storedBrewing = potionBrewing
            // 醸造レシピを集める
            val builder: ImmutableMultimap.Builder<Holder<Potion>, Pair<Holder<Potion>, Ingredient>> = ImmutableMultimap.builder()
            // Vanilla
            for (accessor: PotionBrewingMixAccessor<Potion> in (potionBrewing as PotionBrewingAccessor).potionMixes) {
                val potionFrom: Holder<Potion> = accessor.from
                val potionTo: Holder<Potion> = accessor.to
                builder.put(potionTo, potionFrom to accessor.ingredient)
            }
            // Modded
            for (recipe: BrewingRecipe in potionBrewing.recipes.filterIsInstance<BrewingRecipe>()) {
                val potionFrom: Holder<Potion> = getPotion(recipe.input.items[0])
                val potionTo: Holder<Potion> = getPotion(recipe.output)
                builder.put(potionTo, potionFrom to recipe.ingredient)
            }
            // 醸造レシピを登録していく
            val multimap: ImmutableMultimap<Holder<Potion>, Pair<Holder<Potion>, Ingredient>> = builder.build()
            cachedRecipes = multimap
                .keySet()
                .asSequence()
                .flatMap { potionTo: Holder<Potion> ->
                    multimap[potionTo].mapIndexed { index: Int, (potionFrom: Holder<Potion>, ingredient: Ingredient) ->
                        val fluidIngredient: HTFluidIngredient = when (potionFrom) {
                            Potions.WATER -> HTIngredientCreator.water()
                            else -> listOf(potionFrom)
                                .toHolderSet()
                                .let { HTPotionFluidIngredient(it, HTBottleType.DEFAULT) }
                                .let(HTIngredientCreator::create)
                        }
                        val recipe = HCBrewingRecipe(
                            fluidIngredient,
                            ingredient,
                            BottledPotionContents(potionTo)
                                .let(HCPotionFluidHelper::createFluid)
                                .let(HTResultCreator::create),
                        )
                        HTRecipeHolder(potionTo.toLike().getId().withSuffix("_$index"), recipe)
                    }
                }
            return cachedRecipes
        }

        override fun getId(): ResourceLocation = HiiragiCoreAPI.id("brewing")
    }
}
