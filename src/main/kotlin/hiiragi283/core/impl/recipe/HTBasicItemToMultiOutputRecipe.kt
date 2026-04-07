package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.HTItemToMultiOutputRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTBasicItemToMultiOutputRecipe(val ingredient: HTItemIngredient, results: List<HTItemResult>, time: Int) :
    HTBasicMultiOutputRecipe<SingleRecipeInput>(results, time),
    HTItemToMultiOutputRecipe.Serializable {
    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun getRequiredAmount(input: SingleRecipeInput): Int = ingredient.amount
}
