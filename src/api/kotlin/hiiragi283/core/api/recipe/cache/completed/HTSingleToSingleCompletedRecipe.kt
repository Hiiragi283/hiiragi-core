package hiiragi283.core.api.recipe.cache.completed

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.core.api.recipe.base.HTItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @see mekanism.api.recipes.cache.OneInputCachedRecipe
 */
abstract class HTSingleToSingleCompletedRecipe<INPUT : Any, OUTPUT : Any, RECIPE : HTRecipeFactory<INPUT, OUTPUT>>(
    recipe: RECIPE,
    protected val inputHandler: HTInputHandler<INPUT>,
    protected val outputHandler: HTOutputHandler<OUTPUT>,
    private val amountGetter: (RECIPE, INPUT) -> INPUT,
) : HTCompletedRecipe.WithProgress<RECIPE>(recipe) {
    private val output: OUTPUT = recipe.assemble(inputHandler.getStack())

    override fun canComplete(): Boolean = outputHandler.canInsert(output)

    override fun complete() {
        outputHandler.insert(output)
        amountGetter(recipe, inputHandler.getStack()).let(inputHandler::consume)
    }

    class ItemToFluid(recipe: HTItemToFluidRecipe, inputHandler: HTInputHandler<ItemStack>, outputHandler: HTOutputHandler<FluidStack>) :
        HTSingleToSingleCompletedRecipe<ItemStack, FluidStack, HTItemToFluidRecipe>(
            recipe,
            inputHandler,
            outputHandler,
            HTItemToFluidRecipe::getMatchingStack,
        ) {
        override fun getProgress(): HTProgressData = inputHandler.getStack().let(::SingleRecipeInput).let(recipe::getProgressData)
    }

    class ItemToItem(recipe: HTItemToItemRecipe, inputHandler: HTInputHandler<ItemStack>, outputHandler: HTOutputHandler<ItemStack>) :
        HTSingleToSingleCompletedRecipe<ItemStack, ItemStack, HTItemToItemRecipe>(
            recipe,
            inputHandler,
            outputHandler,
            HTItemToItemRecipe::getMatchingStack,
        ) {
        override fun getProgress(): HTProgressData = inputHandler.getStack().let(::SingleRecipeInput).let(recipe::getProgressData)
    }
}
