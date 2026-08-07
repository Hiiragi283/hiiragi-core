package hiiragi283.core.api.recipe.cache.completed

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.recipe.progress.HTProgressData
import net.minecraft.world.item.ItemStack

/**
 * @see mekanism.api.recipes.cache.OneInputCachedRecipe
 */
abstract class HTSingleToMultiItemCompletedRecipe<INPUT : Any, RECIPE : HTRecipeFactory<INPUT, out Iterable<ItemStack>>>(
    recipe: RECIPE,
    protected val inputHandler: HTInputHandler<INPUT>,
    protected val outputHandler: HTOutputHandler<ItemStack>,
    private val amountGetter: (RECIPE, INPUT) -> INPUT,
) : HTCompletedRecipe.WithProgress<RECIPE>(recipe) {
    private val output: Iterable<ItemStack> = recipe.assemble(inputHandler.getStack())

    override fun canComplete(): Boolean = output.all(outputHandler::canInsert)

    override fun complete() {
        output.forEach(outputHandler::insert)
        amountGetter(recipe, inputHandler.getStack()).let(inputHandler::consume)
    }

    class ItemToItem(recipe: HTItemToMultiItemRecipe, inputHandler: HTInputHandler<ItemStack>, outputHandler: HTOutputHandler<ItemStack>) :
        HTSingleToMultiItemCompletedRecipe<ItemStack, HTItemToMultiItemRecipe>(
            recipe,
            inputHandler,
            outputHandler,
            HTItemToMultiItemRecipe::getMatchingStack,
        ) {
        override fun getProgress(): HTProgressData = inputHandler.getStack().let(recipe::getProgressData)
    }
}
