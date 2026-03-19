package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.FakeRecipeHolder
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.toFake
import hiiragi283.core.common.recipe.base.HTItemToItemRecipe
import hiiragi283.core.impl.recipe.HTLookupRecipeCache
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

object VanillaRecipeTypes {
    @JvmField
    val SMELTING: HTRecipeType.Fake<SingleRecipeInput, HTItemToItemRecipe> = CookingType(RecipeType.SMELTING)

    @JvmField
    val BLASTING: HTRecipeType.Fake<SingleRecipeInput, HTItemToItemRecipe> = CookingType(RecipeType.BLASTING)

    @JvmField
    val SMOKING: HTRecipeType.Fake<SingleRecipeInput, HTItemToItemRecipe> = CookingType(RecipeType.SMOKING)

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
}
