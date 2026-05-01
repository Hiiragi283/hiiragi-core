package hiiragi283.core.api.recipe.base.factory

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Function

interface HTMultiRecipeFactory<INPUT : RecipeInput, OUTPUT : Iterable<*>> : HTRecipeFactory<INPUT, OUTPUT> {
    fun interface FluidTo<OUTPUT : Iterable<*>> :
        HTMultiRecipeFactory<HTSingleFluidRecipeInput, OUTPUT>,
        Function<FluidStack, OUTPUT> {
        override fun apply(input: FluidStack): OUTPUT

        override fun assemble(input: HTSingleFluidRecipeInput): OUTPUT = apply(input.fluid)
    }

    fun interface ItemTo<OUTPUT : Iterable<*>> :
        HTMultiRecipeFactory<SingleRecipeInput, OUTPUT>,
        Function<ItemStack, OUTPUT> {
        override fun apply(input: ItemStack): OUTPUT

        override fun assemble(input: SingleRecipeInput): OUTPUT = apply(input.item())
    }
}
