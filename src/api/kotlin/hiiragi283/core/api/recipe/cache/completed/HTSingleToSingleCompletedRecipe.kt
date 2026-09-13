package hiiragi283.core.api.recipe.cache.completed

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.core.api.recipe.base.HTItemToItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @see mekanism.api.recipes.cache.OneInputCachedRecipe
 */
abstract class HTSingleToSingleCompletedRecipe<INPUT_A : Any, OUTPUT : Any, INPUT : RecipeInput, RECIPE>(
    recipe: RECIPE,
    protected val inputHandler: HTInputHandler<INPUT_A>,
    protected val outputHandler: HTOutputHandler<OUTPUT>,
    private val amountGetter: (RECIPE, INPUT) -> INPUT_A,
) : HTCompletedRecipe.WithProgress<INPUT, RECIPE>(recipe) where RECIPE : HTRecipeFactory<INPUT, OUTPUT>, RECIPE : HTProgressRecipe<INPUT> {
    private val output: OUTPUT = recipe.assemble(input)

    override fun canComplete(): Boolean = outputHandler.canInsert(output)

    override fun complete() {
        outputHandler.insert(output)
        amountGetter(recipe, input).let(inputHandler::consume)
    }

    class ItemToFluid(recipe: HTItemToFluidRecipe, inputHandler: HTInputHandler<ItemStack>, outputHandler: HTOutputHandler<FluidStack>) :
        HTSingleToSingleCompletedRecipe<ItemStack, FluidStack, SingleRecipeInput, HTItemToFluidRecipe>(
            recipe,
            inputHandler,
            outputHandler,
            HTItemToFluidRecipe::getMatchingStack,
        ) {
        override fun createInput(): SingleRecipeInput = SingleRecipeInput(inputHandler.getStack())
    }

    class ItemToItem(recipe: HTItemToItemRecipe, inputHandler: HTInputHandler<ItemStack>, outputHandler: HTOutputHandler<ItemStack>) :
        HTSingleToSingleCompletedRecipe<ItemStack, ItemStack, SingleRecipeInput, HTItemToItemRecipe>(
            recipe,
            inputHandler,
            outputHandler,
            HTItemToItemRecipe::getMatchingStack,
        ) {
        override fun createInput(): SingleRecipeInput = SingleRecipeInput(inputHandler.getStack())
    }
}
