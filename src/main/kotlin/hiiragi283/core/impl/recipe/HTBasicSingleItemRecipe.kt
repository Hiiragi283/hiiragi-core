package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.base.HTSingleItemRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTBasicSingleItemRecipe(val ingredient: HTItemIngredient, val result: HTItemResult, final override val time: Int) :
    HTSingleItemRecipe.Serializable {
    override fun getRequiredAmount(input: SingleRecipeInput): Int = ingredient.amount

    override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())
}
