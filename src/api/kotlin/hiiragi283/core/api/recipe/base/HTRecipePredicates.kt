package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipePredicate
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.common.util.TriPredicate
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

data object HTRecipePredicates {
    //    Single Input    //

    interface SingleInput<INPUT : RecipeInput, INPUT_A : Any> :
        HTRecipePredicate<INPUT>,
        Predicate<INPUT_A> {
        override fun test(input: INPUT_A): Boolean

        fun getMatchingStack(input: INPUT_A): INPUT_A
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

        fun getMatchingStacks(first: INPUT_A, second: INPUT_B): Pair<INPUT_A, INPUT_B>
    }

    interface ItemAndFluid : DoubleInput<HTItemAndFluidRecipeInput, ItemStack, FluidStack> {
        override fun matches(input: HTItemAndFluidRecipeInput): Boolean {
            val (item: ItemStack, fluid: FluidStack) = input
            return test(item, fluid)
        }
    }

    interface DoubleItem : DoubleInput<RecipeInput, ItemStack, ItemStack> {
        override fun matches(input: RecipeInput): Boolean = input.size() >= 2 && test(input.getItem(0), input.getItem(1))
    }

    //    Triple Input    //

    interface TripleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any, INPUT_C : Any> :
        HTRecipePredicate<INPUT>,
        TriPredicate<INPUT_A, INPUT_B, INPUT_C> {
        override fun test(first: INPUT_A, second: INPUT_B, third: INPUT_C): Boolean

        fun getMatchingStacks(first: INPUT_A, second: INPUT_B, third: INPUT_C): Triple<INPUT_A, INPUT_B, INPUT_C>
    }

    interface TripleItem : TripleInput<RecipeInput, ItemStack, ItemStack, ItemStack> {
        override fun matches(input: RecipeInput): Boolean = input.size() >= 3 && test(input.getItem(0), input.getItem(1), input.getItem(2))
    }
}
