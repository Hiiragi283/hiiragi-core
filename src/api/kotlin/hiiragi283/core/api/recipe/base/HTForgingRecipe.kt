package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.base.predicate.HTDoubleRecipePredicate
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import net.minecraft.world.item.ItemStack
import java.util.function.BinaryOperator

interface HTForgingRecipe :
    HTDoubleRecipePredicate.DoubleItem,
    HTRecipeFactory<HTDoubleRecipeInput, ItemStack>,
    HTProgressRecipe<HTDoubleRecipeInput>,
    BinaryOperator<ItemStack> {
    override fun apply(first: ItemStack, second: ItemStack): ItemStack

    override fun assemble(input: HTDoubleRecipeInput): ItemStack {
        val (first: ItemStack, second: ItemStack) = input
        return apply(first, second)
    }
}
