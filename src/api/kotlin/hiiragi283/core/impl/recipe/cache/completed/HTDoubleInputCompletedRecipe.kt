package hiiragi283.core.impl.recipe.cache.completed

import hiiragi283.core.api.recipe.HTBiRecipeFactory
import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.input.HTItemListRecipeInput
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @see mekanism.api.recipes.cache.TwoInputCachedRecipe
 */
abstract class HTDoubleInputCompletedRecipe<
    INPUT_A : Any,
    INPUT_B : Any,
    OUTPUT : Any,
    RECIPE : HTBiRecipeFactory<INPUT_A, INPUT_B, OUTPUT>,
    >(
    recipe: RECIPE,
    protected val firstInputHandler: HTInputHandler<INPUT_A>,
    protected val secondInputHandler: HTInputHandler<INPUT_B>,
    protected val outputHandler: HTOutputHandler<OUTPUT>,
    private val amountGetter: (RECIPE, INPUT_A, INPUT_B) -> Pair<INPUT_A, INPUT_B>,
) : HTCompletedRecipe.WithProgress<RECIPE>(recipe) {
    private val output: OUTPUT = recipe.assemble(firstInputHandler.getStack(), secondInputHandler.getStack())

    override fun canComplete(): Boolean = outputHandler.canInsert(output)

    override fun complete() {
        outputHandler.insert(output)
        amountGetter(recipe, firstInputHandler.getStack(), secondInputHandler.getStack()).let { (first: INPUT_A, second: INPUT_B) ->
            firstInputHandler.consume(first)
            secondInputHandler.consume(second)
        }
    }

    class ItemAndFluid(
        recipe: HTItemAndFluidToItemRecipe,
        firstInputHandler: HTInputHandler<ItemStack>,
        secondInputHandler: HTInputHandler<FluidStack>,
        outputHandler: HTOutputHandler<ItemStack>,
    ) : HTDoubleInputCompletedRecipe<ItemStack, FluidStack, ItemStack, HTItemAndFluidToItemRecipe>(
        recipe,
        firstInputHandler,
        secondInputHandler,
        outputHandler,
        HTItemAndFluidToItemRecipe::getMatchingStacks,
    ) {
        override fun getProgress(): HTProgressData = HTItemAndFluidRecipeInput(firstInputHandler.getStack(), secondInputHandler.getStack()).let(recipe::getProgressData)
    }

    class DoubleItem(
        recipe: HTDoubleItemToItemRecipe,
        firstInputHandler: HTInputHandler<ItemStack>,
        secondInputHandler: HTInputHandler<ItemStack>,
        outputHandler: HTOutputHandler<ItemStack>,
    ) : HTDoubleInputCompletedRecipe<ItemStack, ItemStack, ItemStack, HTDoubleItemToItemRecipe>(
        recipe,
        firstInputHandler,
        secondInputHandler,
        outputHandler,
        HTDoubleItemToItemRecipe::getMatchingStacks,
    ) {
        override fun getProgress(): HTProgressData = listOf(firstInputHandler, secondInputHandler)
            .map(HTInputHandler<ItemStack>::getStack)
            .let(::HTItemListRecipeInput)
            .let(recipe::getProgressData)
    }
}
