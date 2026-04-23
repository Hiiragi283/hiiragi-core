package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.base.HTSingleItemRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTBasicSingleItemRecipe(val ingredient: HTItemIngredient, val result: HTItemResult, final override val time: Int) :
    HTSingleItemRecipe.Serializable {
    override fun getRequiredAmount(input: SingleRecipeInput): Int = ingredient.getRequiredAmount(input.item())

    override fun assemble(input: SingleRecipeInput, preview: Boolean): ItemStack = result.getOrEmpty(preview)

    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())
}
