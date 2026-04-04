package hiiragi283.core.common.recipe

import com.google.common.collect.ImmutableMultimap
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.core.api.data.recipe.ingredient.HTIngredientAccess
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTBottleType
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.FakeRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.toFake
import hiiragi283.core.api.recipe.withSize
import hiiragi283.core.api.registry.toHolderSet
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.common.recipe.base.HTItemToItemRecipe
import hiiragi283.core.common.util.HCPotionFluidHelper
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import hiiragi283.core.impl.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.core.mixin.PotionBrewingAccessor
import hiiragi283.core.mixin.PotionBrewingMixAccessor
import net.minecraft.core.Holder
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.Identifier
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.item.crafting.display.SlotDisplayContext
import net.neoforged.neoforge.common.brewing.BrewingRecipe
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import kotlin.collections.mapIndexed
import kotlin.sequences.flatMap

object VanillaRecipeTypes {
    @JvmField
    val SMELTING: HTRecipeType.Fake<SingleRecipeInput, HTItemToItemRecipe> = CookingType(RecipeType.SMELTING)

    @JvmField
    val BLASTING: HTRecipeType.Fake<SingleRecipeInput, HTItemToItemRecipe> = CookingType(RecipeType.BLASTING)

    @JvmField
    val SMOKING: HTRecipeType.Fake<SingleRecipeInput, HTItemToItemRecipe> = CookingType(RecipeType.SMOKING)

    @JvmField
    val BREWING: HTRecipeType.Fake<HTItemAndFluidRecipeInput, HCBrewingRecipe> = BrewingType

    private class CookingType<RECIPE : AbstractCookingRecipe>(private val recipeType: RecipeType<RECIPE>) :
        HTRecipeType.Fake<SingleRecipeInput, HTItemToItemRecipe> {
        override fun getId(): Identifier = Identifier.parse(recipeType.toString())

        override fun createCache(): HTRecipeCache<SingleRecipeInput, HTItemToItemRecipe> = HTLookupRecipeCache.forRecipe(this)

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<FakeRecipeHolder<HTItemToItemRecipe>> =
            context.getAllRecipes(recipeType).map { holder: RecipeHolder<RECIPE> -> holder.toFake(::CookingRecipe) }
    }

    private class CookingRecipe(val recipe: AbstractCookingRecipe) : HTItemToItemRecipe {
        override fun getRequiredAmount(input: SingleRecipeInput): Int = 1

        override val time: Int = recipe.cookingTime()

        override fun test(input: SingleRecipeInput): Boolean = recipe.input().test(input.item())

        override fun assemble(input: SingleRecipeInput): ItemStack = recipe.assemble(input)
    }

    private data object BrewingType : HTRecipeType.Fake<HTItemAndFluidRecipeInput, HCBrewingRecipe> {
        private var storedBrewing: PotionBrewing = PotionBrewing.EMPTY
        private var cachedRecipes: Sequence<FakeRecipeHolder<HCBrewingRecipe>> = emptySequence()

        override fun createCache(): HTRecipeCache<HTItemAndFluidRecipeInput, HCBrewingRecipe> = HTLookupRecipeCache.forRecipe(this)

        private fun getPotion(stack: ItemStack): Holder<Potion> = HTPotionHelper.getPotion(stack).potion.orElseGet(Potions::WATER)

        override fun getAllRecipes(context: HTRecipeLookup.Context): Sequence<FakeRecipeHolder<HCBrewingRecipe>> {
            val access: RegistryAccess = context.access
            val fluidCreator: HTFluidIngredientCreator = HTIngredientAccess.INSTANCE.fluidCreator(access)
            val contextMap: ContextMap = ContextMap
                .Builder()
                .withParameter(SlotDisplayContext.REGISTRIES, access)
                .create(SlotDisplayContext.CONTEXT)

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
                val potionFrom: Holder<Potion> = getPotion(recipe.input.display().resolveForFirstStack(contextMap))
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
                        val fluidIngredient: SizedFluidIngredient = when (potionFrom) {
                            Potions.WATER -> fluidCreator.water()
                            else -> listOf(potionFrom)
                                .toHolderSet()
                                .let { HTPotionFluidIngredient(it, HTBottleType.DEFAULT) }
                        } withSize FluidType.BUCKET_VOLUME
                        val recipe = HCBrewingRecipe(
                            fluidIngredient,
                            ingredient withSize 1,
                            BottledPotionContents(potionTo)
                                .let(HCPotionFluidHelper::createFluid)
                                .let(FluidStackTemplate::fromNonEmptyStack)
                                .let(HTFluidResult::create),
                        )
                        FakeRecipeHolder(potionTo.toLike().getId().withSuffix("_$index"), recipe)
                    }
                }
            return cachedRecipes
        }

        override fun getId(): Identifier = HiiragiCoreAPI.id("brewing")
    }
}
