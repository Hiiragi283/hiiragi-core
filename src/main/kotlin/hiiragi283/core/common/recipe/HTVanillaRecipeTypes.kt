package hiiragi283.core.common.recipe

import com.google.common.collect.ImmutableMultimap
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.data.recipe.HTResultCreator
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.findFirst
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.IdToValue
import hiiragi283.core.api.resource.toId
import hiiragi283.core.mixin.PotionBrewingAccessor
import hiiragi283.core.mixin.PotionBrewingMixAccessor
import hiiragi283.core.util.HCPotionFluidHelper
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.brewing.BrewingRecipe

/**
 * @see RecipeType
 */
object HTVanillaRecipeTypes {
    @JvmField
    val SMELTING: HTRecipeType.Managed<SingleRecipeInput, SmeltingRecipe> = CookingType(RecipeType.SMELTING)

    @JvmField
    val BLASTING: HTRecipeType.Managed<SingleRecipeInput, BlastingRecipe> = CookingType(RecipeType.BLASTING)

    @JvmField
    val SMOKING: HTRecipeType.Managed<SingleRecipeInput, SmokingRecipe> = CookingType(RecipeType.SMOKING)

    private class CookingType<RECIPE : AbstractCookingRecipe>(private val recipeType: RecipeType<RECIPE>) :
        HTRecipeType.Managed<SingleRecipeInput, RECIPE> {
        override fun getId(): ResourceLocation = ResourceLocation.parse(recipeType.toString())

        override fun createCache(): HTRecipeCache<SingleRecipeInput, RECIPE> = HTLookupRecipeCache.forManager(this)

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<RecipeHolder<RECIPE>> = context.getAllRecipes(recipeType)
    }

    @JvmField
    val BREWING: HTRecipeType.Fake<HTItemAndFluidRecipeInput, HCBrewingRecipe> = BrewingType

    private data object BrewingType : HTRecipeType.Fake<HTItemAndFluidRecipeInput, HCBrewingRecipe> {
        private val storedBrewing: PotionBrewing = PotionBrewing.EMPTY
        private val cachedRecipes: Sequence<IdToValue<HCBrewingRecipe>> = emptySequence()

        override fun createCache(): HTRecipeCache<HTItemAndFluidRecipeInput, HCBrewingRecipe> =
            HTRecipeCache { input: HTItemAndFluidRecipeInput, level: Level -> findFirst(input, level)?.second }

        private fun getPotion(stack: ItemStack): Holder<Potion> = HTPotionHelper.getPotion(stack).potion.orElse(Potions.WATER)

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<IdToValue<HCBrewingRecipe>> {
            // すでにレシピが生成されている場合はパス
            val potionBrewing: PotionBrewing = context.brewing
            if (potionBrewing == storedBrewing && cachedRecipes.any()) {
                return cachedRecipes
            }
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
            return multimap
                .keySet()
                .asSequence()
                .flatMap { potionTo: Holder<Potion> ->
                    multimap[potionTo].mapIndexed { index: Int, (potionFrom: Holder<Potion>, ingredient: Ingredient) ->
                        val resultContents: HTPotionContents = HTPotionContents.of(potionTo, HTBottleType.DEFAULT) ?: return@mapIndexed null
                        val fluidIngredient: HTFluidIngredient = when (potionFrom) {
                            Potions.WATER -> HTIngredientCreator.water(1000)
                            else -> HTIngredientCreator.create(HTPotionFluidIngredient(HolderSet.direct(potionFrom), HTBottleType.DEFAULT))
                        }
                        val recipe = HCBrewingRecipe(
                            fluidIngredient,
                            HTIngredientCreator.create(ingredient),
                            HTResultCreator.create(HCPotionFluidHelper.createFluid(resultContents)),
                        )
                        potionTo.toLike().getId().withSuffix("_$index") to recipe
                    }
                }.filterNotNull()
        }

        override fun getId(): ResourceLocation = HTConst.MINECRAFT.toId("brewing")
    }
}
