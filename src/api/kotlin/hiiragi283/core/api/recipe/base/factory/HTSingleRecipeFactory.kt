package hiiragi283.core.api.recipe.base.factory

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Function

interface HTSingleRecipeFactory<INPUT : RecipeInput, INPUT_A : Any, OUTPUT : Any> :
    HTRecipeFactory<INPUT, OUTPUT>,
    Function<INPUT_A, OUTPUT> {
    fun getRequiredAmount(input: INPUT_A): Int

    interface FluidTo<OUTPUT : Any> : HTSingleRecipeFactory<HTSingleFluidRecipeInput, FluidStack, OUTPUT> {
        override fun apply(input: FluidStack): OUTPUT

        override fun assemble(input: HTSingleFluidRecipeInput): OUTPUT = apply(input.fluid)
    }

    interface ItemTo<OUTPUT : Any> : HTSingleRecipeFactory<SingleRecipeInput, ItemStack, OUTPUT> {
        override fun apply(input: ItemStack): OUTPUT

        override fun assemble(input: SingleRecipeInput): OUTPUT = apply(input.item())
    }
}
