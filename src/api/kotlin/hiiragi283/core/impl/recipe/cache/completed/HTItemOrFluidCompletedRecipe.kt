package hiiragi283.core.impl.recipe.cache.completed

import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTItemOrFluidCompletedRecipe(
    recipe: HTItemOrFluidRecipe,
    private val itemInputHandler: HTInputHandler<ItemStack>,
    private val fluidInputHandler: HTInputHandler<FluidStack>,
    private val itemOutputHandler: HTOutputHandler<ItemStack>,
    private val fluidOutputHandler: HTOutputHandler<FluidStack>,
) : HTCompletedRecipe.WithProgress<HTItemOrFluidRecipe>(recipe) {
    val output: Ior<ItemStack, FluidStack> = recipe.assemble(itemInputHandler.getStack(), fluidInputHandler.getStack())

    override fun getProgress(): HTProgressData = HTItemAndFluidRecipeInput(itemInputHandler.getStack(), fluidInputHandler.getStack())
        .let(recipe::getProgressData)

    override fun canComplete(): Boolean = output.map(
        itemOutputHandler::canInsert,
        fluidOutputHandler::canInsert,
    ) { item: Boolean, fluid: Boolean -> item && fluid }

    override fun complete() {
        // outputs
        output.getLeft()?.let(itemOutputHandler::insert)
        output.getRight()?.let(fluidOutputHandler::insert)
        // inputs
        recipe.getRequiredAmount(itemInputHandler.getStack(), fluidInputHandler.getStack()).let { (item: Int, fluid: Int) ->
            itemInputHandler.consume(item)
            fluidInputHandler.consume(fluid)
        }
    }
}
