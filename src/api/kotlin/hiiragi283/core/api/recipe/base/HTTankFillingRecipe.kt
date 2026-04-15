package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

interface HTTankFillingRecipe : HTRecipe<HTItemAndFluidRecipeInput> {
    fun testContainer(stack: ItemStack): Boolean

    fun testFluid(stack: FluidStack): Boolean

    fun getRequiredFluidAmount(input: HTItemAndFluidRecipeInput): Int

    //    HTRecipe    //

    override fun test(input: HTItemAndFluidRecipeInput): Boolean = testContainer(input.item) && testFluid(input.fluid)

    //    Serializable    //

    interface Serializable :
        HTTankFillingRecipe,
        HTSerializableRecipe<HTItemAndFluidRecipeInput>
}
