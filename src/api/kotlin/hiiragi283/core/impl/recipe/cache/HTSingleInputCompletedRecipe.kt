package hiiragi283.core.impl.recipe.cache

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import java.util.function.Function

/**
 * @see mekanism.api.recipes.cache.OneInputCachedRecipe
 */
class HTSingleInputCompletedRecipe<INPUT : Any, OUTPUT : Any, RECIPE : Function<INPUT, OUTPUT>>(
    recipe: RECIPE,
    private val inputHandler: HTInputHandler<INPUT>,
    private val outputHandler: HTOutputHandler<OUTPUT>,
    private val amountGetter: (RECIPE, INPUT) -> Int,
) : HTCompletedRecipe<RECIPE>(recipe) {
    private val output: OUTPUT = recipe.apply(inputHandler.getStack())

    override fun canComplete(): Boolean = outputHandler.canInsert(output)

    override fun complete() {
        outputHandler.insert(output)
        amountGetter(recipe, inputHandler.getStack()).let(inputHandler::consume)
    }
}
