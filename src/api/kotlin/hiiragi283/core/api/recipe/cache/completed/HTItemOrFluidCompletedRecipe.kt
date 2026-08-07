package hiiragi283.core.api.recipe.cache.completed

import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.recipe.progress.HTProgressData
import hiiragi283.core.api.recipe.result.HTItemAndFluidResult
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTItemOrFluidCompletedRecipe(
    recipe: HTItemOrFluidRecipe,
    private val itemInputHandler: HTInputHandler<ItemStack>,
    private val fluidInputHandler: HTInputHandler<FluidStack>,
    private val itemOutputHandler: HTOutputHandler<ItemStack>,
    private val fluidOutputHandler: HTOutputHandler<FluidStack>,
) : HTCompletedRecipe.WithProgress<HTItemOrFluidRecipe>(recipe) {
    val output: HTItemAndFluidResult = recipe.assemble(itemInputHandler.getStack(), fluidInputHandler.getStack())

    override fun getProgress(): HTProgressData = recipe.getProgressData(itemInputHandler.getStack(), fluidInputHandler.getStack())

    override fun canComplete(): Boolean = output.let { (item: ItemStack, fluid: FluidStack) -> itemOutputHandler.canInsert(item) && fluidOutputHandler.canInsert(fluid) }

    override fun complete() {
        // outputs
        output.let { (item: ItemStack, fluid: FluidStack) ->
            itemOutputHandler.insert(item)
            fluidOutputHandler.insert(fluid)
        }
        // inputs
        recipe.getMatchingStacks(itemInputHandler.getStack(), fluidInputHandler.getStack()).let { (item: ItemStack, fluid: FluidStack) ->
            itemInputHandler.consume(item)
            fluidInputHandler.consume(fluid)
        }
    }
}
