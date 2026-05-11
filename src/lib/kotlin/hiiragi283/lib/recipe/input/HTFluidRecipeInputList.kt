package hiiragi283.lib.recipe.input

import net.neoforged.neoforge.fluids.FluidStack

data class HTFluidRecipeInputList(private val input: HTFluidRecipeInput) : AbstractList<FluidStack>() {
    override val size: Int get() = input.getFluidSize()

    override fun get(index: Int): FluidStack = input.getFluid(index)
}
