package hiiragi283.core.common.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.collection.MultiMap
import hiiragi283.lib.collection.buildListMultiMap
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.ingredient.getRequiredAmount
import hiiragi283.lib.recipe.ingredient.test
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookupContext
import hiiragi283.lib.registry.createKey
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.registry.toLike
import net.minecraft.core.Holder
import net.minecraft.core.TypedInstance
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

data object VanillaRecipeLookups {
    @JvmField
    val SMELTING: HTRecipeLookup.Translatable<HTItemToItemRecipe> = CookingLookup(RecipeType.SMELTING)

    @JvmField
    val BLASTING: HTRecipeLookup.Translatable<HTItemToItemRecipe> = CookingLookup(RecipeType.BLASTING)

    @JvmField
    val SMOKING: HTRecipeLookup.Translatable<HTItemToItemRecipe> = CookingLookup(RecipeType.SMOKING)

    @JvmInline
    private value class CookingLookup<RECIPE : AbstractCookingRecipe>(private val recipeType: RecipeType<RECIPE>) : HTRecipeLookup.Translatable<HTItemToItemRecipe> {
        override fun getAllRecipes(contextMap: ContextMap): Map<RecipeKey, HTItemToItemRecipe> = contextMap.getOrThrow(HTRecipeLookupContext.RECIPES)
            .byType(recipeType)
            .associate { holder: RecipeHolder<RECIPE> -> holder.id() to HCCookingRecipe(holder.value()) }

        override fun getKey(): ResourceKey<RecipeType<*>> = BuiltInRegistries.RECIPE_TYPE.wrapAsHolder(recipeType).getKeyOrThrow()
    }

    @JvmInline
    private value class HCCookingRecipe(private val recipe: AbstractCookingRecipe) : HTItemToItemRecipe {
        override fun test(input: TypedInstance<Item>): Boolean = recipe.input().test(input)

        override fun getRequiredAmount(input: TypedInstance<Item>): Int = recipe.input().getRequiredAmount(input)

        override fun assemble(input: ItemInstance): ItemStack = HTIngredientHelper.createStack(input).let(::SingleRecipeInput).let(recipe::assemble)

        override fun getProgressData(input: SingleRecipeInput): HTProgressData = HTProgressData.time(recipe.cookingTime())
    }

    @JvmField
    val BREWING: HTRecipeLookup.Translatable<HCBrewingRecipe> = BrewingLookup

    private data object BrewingLookup : HTRecipeLookup.Translatable<HCBrewingRecipe> {
        override fun getAllRecipes(contextMap: ContextMap): Map<RecipeKey, HCBrewingRecipe> {
            val multiMap: MultiMap<Holder<Potion>, HCBrewingRecipe> = buildListMultiMap {
                contextMap.getOptional(HTRecipeLookupContext.BREWING)
                    ?.let(PotionBrewing::potionMixes)
                    ?.map(::HCBrewingRecipe)
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
