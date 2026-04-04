package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.base.HTItemToChancedRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.Optional

abstract class HTBasicItemToChancedRecipe(
    val ingredient: SizedIngredient,
    result: HTItemResult,
    extraResult: Optional<HTItemResult>,
    time: Int,
) : HTBasicChancedRecipe<SingleRecipeInput>(result, extraResult, time),
    HTItemToChancedRecipe.Serializable {
    final override fun getRequiredAmount(input: SingleRecipeInput): Int = ingredient.count()

    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())
}
