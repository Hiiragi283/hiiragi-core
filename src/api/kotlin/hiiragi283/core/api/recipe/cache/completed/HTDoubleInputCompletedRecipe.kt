package hiiragi283.core.api.recipe.cache.completed

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @see mekanism.api.recipes.cache.TwoInputCachedRecipe
 */
abstract class HTDoubleInputCompletedRecipe<
    INPUT_A : Any,
    INPUT_B : Any,
    OUTPUT : Any,
    INPUT : RecipeInput,
    RECIPE,
    >(
    recipe: RECIPE,
    protected val firstInputHandler: HTInputHandler<INPUT_A>,
    protected val secondInputHandler: HTInputHandler<INPUT_B>,
    protected val outputHandler: HTOutputHandler<OUTPUT>,
    private val amountGetter: (RECIPE, INPUT) -> Pair<INPUT_A, INPUT_B>,
) : HTCompletedRecipe.WithProgress<INPUT, RECIPE>(recipe) where RECIPE : HTRecipeFactory<INPUT, OUTPUT>, RECIPE : HTProgressRecipe<INPUT> {
    private val output: OUTPUT = recipe.assemble(input)

    override fun canComplete(): Boolean = outputHandler.canInsert(output)

    override fun complete() {
        outputHandler.insert(output)
        amountGetter(recipe, input).let { (first: INPUT_A, second: INPUT_B) ->
            firstInputHandler.consume(first)
            secondInputHandler.consume(second)
        }
    }

    class ItemAndFluid(
        recipe: HTItemAndFluidToItemRecipe,
        firstInputHandler: HTInputHandler<ItemStack>,
        secondInputHandler: HTInputHandler<FluidStack>,
        outputHandler: HTOutputHandler<ItemStack>,
    ) : HTDoubleInputCompletedRecipe<ItemStack, FluidStack, ItemStack, HTItemAndFluidRecipeInput, HTItemAndFluidToItemRecipe>(
        recipe,
        firstInputHandler,
        secondInputHandler,
        outputHandler,
        HTItemAndFluidToItemRecipe::getMatchingStacks,
    ) {
        override fun createInput(): HTItemAndFluidRecipeInput = HTItemAndFluidRecipeInput(firstInputHandler.getStack(), secondInputHandler.getStack())
    }

    class DoubleItem(
        recipe: HTDoubleItemToItemRecipe,
        firstInputHandler: HTInputHandler<ItemStack>,
        secondInputHandler: HTInputHandler<ItemStack>,
        outputHandler: HTOutputHandler<ItemStack>,
    ) : HTDoubleInputCompletedRecipe<ItemStack, ItemStack, ItemStack, RecipeInput, HTDoubleItemToItemRecipe>(
        recipe,
        firstInputHandler,
        secondInputHandler,
        outputHandler,
        HTDoubleItemToItemRecipe::getMatchingStacks,
    ) {
        override fun createInput(): RecipeInput = TODO()
    }
}
