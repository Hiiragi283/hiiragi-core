package hiiragi283.core.api.recipe.base.predicate

import hiiragi283.core.api.recipe.HTRecipePredicate
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

interface HTSingleRecipePredicate<INPUT : RecipeInput, INPUT_A : Any> :
    HTRecipePredicate<INPUT>,
    Predicate<INPUT_A> {
    override fun test(input: INPUT_A): Boolean

    fun getRequiredAmount(input: INPUT_A): Int

    interface SingleFluid : HTSingleRecipePredicate<HTSingleFluidRecipeInput, FluidStack> {
        override fun matches(input: HTSingleFluidRecipeInput): Boolean = test(input.fluid)
    }

    interface SingleItem : HTSingleRecipePredicate<SingleRecipeInput, ItemStack> {
        override fun matches(input: SingleRecipeInput): Boolean = test(input.item())
    }
}
