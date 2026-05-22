package hiiragi283.lib.recipe.base

import hiiragi283.lib.recipe.HTRecipePredicate
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.common.util.TriPredicate
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.fluids.FluidInstance

data object HTRecipePredicates {
    //    Single Input    //

    interface SingleInput<INPUT : RecipeInput, INPUT_A : Any> :
        HTRecipePredicate<INPUT>,
        Predicate<INPUT_A> {
        override fun test(input: INPUT_A): Boolean

        fun getRequiredAmount(input: INPUT_A): Int
    }

    interface SingleFluid : SingleInput<HTSingleFluidRecipeInput, FluidInstance> {
        override fun matches(input: HTSingleFluidRecipeInput): Boolean = test(input.fluid)
    }

    interface SingleItem : SingleInput<SingleRecipeInput, ItemInstance> {
        override fun matches(input: SingleRecipeInput): Boolean = test(input.item())
    }

    //    Double Input    //

    interface DoubleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any> :
        HTRecipePredicate<INPUT>,
        BiPredicate<INPUT_A, INPUT_B> {
        override fun test(first: INPUT_A, second: INPUT_B): Boolean

        fun getRequiredAmount(first: INPUT_A, second: INPUT_B): Pair<Int, Int>
    }

    interface ItemAndFluid : DoubleInput<HTItemAndFluidRecipeInput, ItemInstance, FluidInstance> {
        override fun matches(input: HTItemAndFluidRecipeInput): Boolean {
            val (item: ItemInstance, fluid: FluidInstance) = input
            return test(item, fluid)
        }
    }

    interface DoubleItem : DoubleInput<RecipeInput, ItemInstance, ItemInstance> {
        override fun matches(input: RecipeInput): Boolean {
            if (input.size() < 2) return false
            return test(input.getItem(0), input.getItem(1))
        }
    }

    //    Triple Input    //

    interface TripleInput<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any, INPUT_C : Any> :
        HTRecipePredicate<INPUT>,
        TriPredicate<INPUT_A, INPUT_B, INPUT_C> {
        override fun test(first: INPUT_A, second: INPUT_B, third: INPUT_C): Boolean

        fun getRequiredAmount(first: INPUT_A, second: INPUT_B, third: INPUT_C): Triple<Int, Int, Int>
    }

    interface TripleItem : TripleInput<RecipeInput, ItemInstance, ItemInstance, ItemInstance> {
        override fun matches(input: RecipeInput): Boolean {
            if (input.size() < 3) return false
            return test(input.getItem(0), input.getItem(1), input.getItem(2))
        }
    }
}
