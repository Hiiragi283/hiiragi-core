package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.HTItemToChancedRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.SingleRecipeInput
import java.util.Optional

abstract class HTBasicItemToChancedRecipe(
    val ingredient: HTItemIngredient,
    result: HTItemResult,
    extraResult: Optional<HTItemResult>,
    time: Int,
) : HTBasicChancedRecipe<SingleRecipeInput>(result, extraResult, time),
    HTItemToChancedRecipe.Serializable {
    final override fun getRequiredAmount(input: SingleRecipeInput): Int = ingredient.amount

    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())
}
