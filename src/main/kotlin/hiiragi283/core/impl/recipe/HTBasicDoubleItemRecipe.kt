package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.base.HTDoubleItemRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.world.item.ItemStack
import java.util.Optional

abstract class HTBasicDoubleItemRecipe(
    val base: HTItemIngredient,
    val addition: Optional<HTItemIngredient>,
    val result: HTItemResult,
    final override val time: Int,
) : HTDoubleItemRecipe.Serializable {
    final override fun getBaseAmount(input: HTDoubleRecipeInput): Int = base.amount

    final override fun getAdditionAmount(input: HTDoubleRecipeInput): Int = addition.map(HTItemIngredient::amount).orElseGet { 0 }

    final override fun test(input: HTDoubleRecipeInput): Boolean {
        val (first: ItemStack, second: ItemStack) = input
        return base.test(first) && addition.map { it.test(second) }.orElseGet { true }
    }

    final override fun assemble(input: HTDoubleRecipeInput, preview: Boolean): ItemStack = result.getOrEmpty(preview)
}
