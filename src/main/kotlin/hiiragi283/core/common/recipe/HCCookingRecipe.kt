package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTPhysicalSideHelper
import hiiragi283.core.api.recipe.base.HTSingleItemRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput

class HCCookingRecipe(val recipe: AbstractCookingRecipe) : HTSingleItemRecipe {
    override fun getRequiredAmount(input: SingleRecipeInput): Int = 1

    override val time: Int = recipe.cookingTime

    override fun test(input: SingleRecipeInput): Boolean = recipe.ingredients[0].test(input.item())

    override fun assemble(input: SingleRecipeInput, preview: Boolean): ItemStack =
        HTPhysicalSideHelper.getRegistryAccess()?.let { recipe.assemble(input, it) } ?: ItemStack.EMPTY
}
