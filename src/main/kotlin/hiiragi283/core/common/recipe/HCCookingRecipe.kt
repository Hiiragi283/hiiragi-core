package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.HTItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.getRequiredAmount
import hiiragi283.core.mixin.AbstractCookingRecipeAccessor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCCookingRecipe(private val recipe: AbstractCookingRecipe) : HTItemToItemRecipe {
    private val accessor: AbstractCookingRecipeAccessor = recipe as AbstractCookingRecipeAccessor
    private val ingredient: Ingredient = accessor.ingredient

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getRequiredAmount(input: ItemStack): Int = ingredient.getRequiredAmount(input)

    override fun assemble(input: ItemStack): ItemStack = accessor.result.copy()

    override fun getProgressData(input: SingleRecipeInput): HTProgressData = HTProgressData.time(recipe.cookingTime)
}
