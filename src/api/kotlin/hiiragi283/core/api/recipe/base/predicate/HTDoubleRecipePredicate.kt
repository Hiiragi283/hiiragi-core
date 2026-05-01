package hiiragi283.core.api.recipe.base.predicate

import hiiragi283.core.api.recipe.HTRecipePredicate
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate

interface HTDoubleRecipePredicate<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any> :
    HTRecipePredicate<INPUT>,
    BiPredicate<INPUT_A, INPUT_B> {
    override fun test(first: INPUT_A, second: INPUT_B): Boolean

    fun getRequiredAmount(first: INPUT_A, second: INPUT_B): Pair<Int, Int>

    interface ItemAndFluid : HTDoubleRecipePredicate<HTItemAndFluidRecipeInput, ItemStack, FluidStack> {
        override fun matches(input: HTItemAndFluidRecipeInput): Boolean {
            val (item: ItemStack, fluid: FluidStack) = input
            return test(item, fluid)
        }
    }

    interface DoubleItem : HTDoubleRecipePredicate<HTDoubleRecipeInput, ItemStack, ItemStack> {
        override fun matches(input: HTDoubleRecipeInput): Boolean {
            val (first: ItemStack, second: ItemStack) = input
            return test(first, second)
        }
    }
}
