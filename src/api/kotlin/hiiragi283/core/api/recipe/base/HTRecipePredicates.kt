package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipePredicate
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

data object HTRecipePredicates {
    //    Single Input    //

    interface SingleInput<INPUT : RecipeInput, INPUT_A : Any> :
        HTRecipePredicate<INPUT>,
        Predicate<INPUT_A> {
        override fun test(input: INPUT_A): Boolean

        fun getRequiredAmount(input: INPUT_A): Int
    }

    interface SingleFluid : SingleInput<HTSingleFluidRecipeInput, FluidStack> {
        override fun matches(input: HTSingleFluidRecipeInput): Boolean = test(input.fluid)
    }

    interface SingleItem : SingleInput<SingleRecipeInput, ItemStack> {
        override fun matches(input: SingleRecipeInput): Boolean = test(input.item())
    }

    //    Double Input    //

    interface DoubleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any> :
        HTRecipePredicate<INPUT>,
        BiPredicate<INPUT_A, INPUT_B> {
        override fun test(first: INPUT_A, second: INPUT_B): Boolean

        fun getRequiredAmount(first: INPUT_A, second: INPUT_B): Pair<Int, Int>
    }

    interface ItemAndFluid : DoubleInput<HTItemAndFluidRecipeInput, ItemStack, FluidStack> {
        override fun matches(input: HTItemAndFluidRecipeInput): Boolean {
            val (item: ItemStack, fluid: FluidStack) = input
            return test(item, fluid)
        }
    }

    interface DoubleItem : DoubleInput<HTDoubleRecipeInput, ItemStack, ItemStack> {
        override fun matches(input: HTDoubleRecipeInput): Boolean {
            val (first: ItemStack, second: ItemStack) = input
            return test(first, second)
        }
    }
}
