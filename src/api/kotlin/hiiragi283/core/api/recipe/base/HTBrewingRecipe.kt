package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

interface HTBrewingRecipe :
    HTItemOrFluidRecipe,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
    override fun getRequiredAmount(first: ItemStack, second: FluidStack): Pair<Int, Int> = when {
        test(first, second) -> 1 to HTConst.DEFAULT_FLUID_AMOUNT
        else -> 0 to 0
    }

    override val progressData: HTProgressData
        get() = HTProgressData.time(200)
}
