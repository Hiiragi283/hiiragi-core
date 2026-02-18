package hiiragi283.core.common.recipe

import com.google.common.collect.ImmutableMultimap
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.data.recipe.HTResultCreator
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeFinder
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.registry.toLike
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
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.brewing.BrewingRecipe
import kotlin.jvm.optionals.getOrNull

/**
 * @see RecipeType
 */
object HTVanillaRecipeTypes {
    @JvmField
    val SMELTING: HTRecipeType<SingleRecipeInput, SmeltingRecipe> = VanillaLookup(RecipeType.SMELTING)

    @JvmField
    val BLASTING: HTRecipeType<SingleRecipeInput, BlastingRecipe> = VanillaLookup(RecipeType.BLASTING)

    @JvmField
    val SMOKING: HTRecipeType<SingleRecipeInput, SmokingRecipe> = VanillaLookup(RecipeType.SMOKING)

    private class VanillaLookup<INPUT : RecipeInput, RECIPE : Recipe<INPUT>>(private val recipeType: RecipeType<RECIPE>) :
        HTRecipeType<INPUT, RECIPE> {
        override fun createCache(): HTRecipeCache<INPUT, RECIPE> = HTFinderRecipeCache(
            HTRecipeFinder.Vanilla { input: INPUT, level: Level, lastRecipe: RecipeHolder<RECIPE>? ->
                level.recipeManager
                    .getRecipeFor(recipeType, input, level, lastRecipe)
                    .getOrNull()
            },
        )

        override fun getAllRecipes(context: HTRecipeLookup.Context): List<RecipeHolder<RECIPE>> =
            context.manager.getAllRecipesFor(recipeType)

        override fun getId(): ResourceLocation = ResourceLocation.parse(recipeType.toString())
    }

    @JvmField
    val BREWING: HTRecipeType<HTItemAndFluidRecipeInput, HCBrewingRecipe> = BrewingType

    private data object BrewingType : HTRecipeType<HTItemAndFluidRecipeInput, HCBrewingRecipe> {
        private val storedBrewing: PotionBrewing = PotionBrewing.EMPTY
        private val cachedRecipes: List<RecipeHolder<HCBrewingRecipe>> = emptyList()

        override fun createCache(): HTRecipeCache<HTItemAndFluidRecipeInput, HCBrewingRecipe> =
            HTRecipeCache { input: HTItemAndFluidRecipeInput, level: Level -> findFirst(level) { it.matches(input, level) }?.value() }

        private fun getPotion(stack: ItemStack): Holder<Potion> = HTPotionHelper.getPotion(stack).potion.orElse(Potions.WATER)

        override fun getAllRecipes(context: HTRecipeLookup.Context): List<RecipeHolder<HCBrewingRecipe>> {
            // すでにレシピが生成されている場合はパス
            val potionBrewing: PotionBrewing = context.brewing
            if (potionBrewing == storedBrewing && cachedRecipes.isNotEmpty()) {
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
                        RecipeHolder(potionTo.toLike().getId().withSuffix("_$index"), recipe)
                    }
                }.filterNotNull()
        }

        override fun getId(): ResourceLocation = HTConst.MINECRAFT.toId("brewing")
    }
}
