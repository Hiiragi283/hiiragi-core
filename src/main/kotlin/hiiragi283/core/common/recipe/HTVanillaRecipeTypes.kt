package hiiragi283.core.common.recipe

import com.google.common.collect.ImmutableMultimap
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.getRequiredAmount
import hiiragi283.lib.recipe.ingredient.test
import hiiragi283.lib.recipe.lookup.HTRecipeLookupContext
import hiiragi283.lib.recipe.lookup.HTRecipeType
import hiiragi283.lib.registry.toLike
import net.minecraft.core.Holder
import net.minecraft.core.TypedInstance
import net.minecraft.resources.Identifier
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

object HTVanillaRecipeTypes {
    @JvmField
    val SMELTING: HTRecipeType<HTItemToItemRecipe> = CookingType(RecipeType.SMELTING)

    @JvmField
    val BLASTING: HTRecipeType<HTItemToItemRecipe> = CookingType(RecipeType.BLASTING)

    @JvmField
    val SMOKING: HTRecipeType<HTItemToItemRecipe> = CookingType(RecipeType.SMOKING)

    @JvmInline
    private value class CookingType<RECIPE : AbstractCookingRecipe>(private val recipeType: RecipeType<RECIPE>) : HTRecipeType<HTItemToItemRecipe> {
        override fun getAllRecipes(contextMap: ContextMap): Sequence<HTRecipeHolder<HTItemToItemRecipe>> = contextMap.getOrThrow(HTRecipeLookupContext.RECIPES)
            .byType(recipeType)
            .asSequence()
            .map(HTRecipeHolder.Companion::from)
            .map { it.mapRecipe(::HCCookingRecipe) }

        override fun getId(): Identifier = Identifier.parse(recipeType.toString())
    }

    @JvmInline
    private value class HCCookingRecipe(private val recipe: AbstractCookingRecipe) : HTItemToItemRecipe {
        override fun test(input: TypedInstance<Item>): Boolean = recipe.input().test(input)

        override fun getRequiredAmount(input: TypedInstance<Item>): Int = recipe.input().getRequiredAmount(input)

        override fun assemble(input: ItemInstance): ItemStack = HTIngredientHelper.createStack(input).let(::SingleRecipeInput).let(recipe::assemble)

        override fun getProgressData(input: SingleRecipeInput): HTProgressData = HTProgressData.time(recipe.cookingTime())
    }

    @JvmField
    val BREWING: HTRecipeType<HCBrewingRecipe> = BrewingType

    private data object BrewingType : HTRecipeType<HCBrewingRecipe> {
        override fun getAllRecipes(contextMap: ContextMap): Sequence<HTRecipeHolder<HCBrewingRecipe>> {
            val builder: ImmutableMultimap.Builder<Holder<Potion>, HCBrewingRecipe> = ImmutableMultimap.builder()
            val recipes: List<HCBrewingRecipe> = contextMap.getOptional(HTRecipeLookupContext.BREWING)?.let(PotionBrewing::potionMixes)?.map(::HCBrewingRecipe) ?: return emptySequence()
            recipes.forEach { builder.put(it.potionTo, it) }
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

        override fun getId(): Identifier = HiiragiCoreAPI.id("brewing")
    }
}
