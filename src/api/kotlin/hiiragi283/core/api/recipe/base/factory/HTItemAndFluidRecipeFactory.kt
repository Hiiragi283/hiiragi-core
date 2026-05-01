package hiiragi283.core.api.recipe.base.factory

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Function

fun interface HTItemAndFluidRecipeFactory<INPUT : RecipeInput> : HTRecipeFactory<INPUT, Ior<ItemStack, FluidStack>> {
    fun interface SingleItemTo :
        HTItemAndFluidRecipeFactory<SingleRecipeInput>,
        Function<ItemStack, Ior<ItemStack, FluidStack>> {
        override fun apply(input: ItemStack): Ior<ItemStack, FluidStack>

        override fun assemble(input: SingleRecipeInput): Ior<ItemStack, FluidStack> = apply(input.item())
    }
}
