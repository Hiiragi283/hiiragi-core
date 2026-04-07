package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.world.item.ItemStack
import java.util.Optional

abstract class HTBasicDoubleMultiOutputRecipe(
    val base: HTItemIngredient,
    val addition: Optional<HTItemIngredient>,
    results: List<HTItemResult>,
    time: Int,
) : HTBasicMultiOutputRecipe<HTDoubleRecipeInput>(results, time),
    HTDoubleMultiOutputRecipe.Serializable {
    final override fun test(input: HTDoubleRecipeInput): Boolean {
        val (first: ItemStack, second: ItemStack) = input
        return base.test(first) && addition.map { it.test(second) }.orElseGet { true }
    }

    final override fun getBaseAmount(input: HTDoubleRecipeInput): Int = base.amount

    final override fun getAdditionAmount(input: HTDoubleRecipeInput): Int = addition.map(HTItemIngredient::amount).orElseGet { 0 }
}
