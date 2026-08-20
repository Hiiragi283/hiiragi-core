package hiiragi283.core.api.recipe.cache.completed

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * @see mekanism.api.recipes.cache.OneInputCachedRecipe
 */
abstract class HTSingleToMultiItemCompletedRecipe<INPUT_A : Any, INPUT : RecipeInput, RECIPE>(
    recipe: RECIPE,
    protected val inputHandler: HTInputHandler<INPUT_A>,
    protected val outputHandler: HTOutputHandler<ItemStack>,
    private val amountGetter: (RECIPE, INPUT) -> INPUT_A,
) : HTCompletedRecipe.WithProgress<INPUT, RECIPE>(recipe) where RECIPE : HTRecipeFactory<INPUT, Iterable<ItemStack>>, RECIPE : HTProgressRecipe<INPUT> {
    private val output: Iterable<ItemStack> = recipe.assemble(input)

    override fun canComplete(): Boolean = output.all(outputHandler::canInsert)

    override fun complete() {
        output.forEach(outputHandler::insert)
        amountGetter(recipe, input).let(inputHandler::consume)
    }

    class ItemToItem(recipe: HTItemToMultiItemRecipe, inputHandler: HTInputHandler<ItemStack>, outputHandler: HTOutputHandler<ItemStack>) :
        HTSingleToMultiItemCompletedRecipe<ItemStack, SingleRecipeInput, HTItemToMultiItemRecipe>(
            recipe,
            inputHandler,
            outputHandler,
            HTItemToMultiItemRecipe::getMatchingStack,
        ) {
        override fun createInput(): SingleRecipeInput = SingleRecipeInput(inputHandler.getStack())
    }
}
