package hiiragi283.core.impl.recipe.cache

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import java.util.function.BiFunction

class HTDoubleInputCompletedRecipe<INPUT_A : Any, INPUT_B : Any, OUTPUT : Any, RECIPE : BiFunction<INPUT_A, INPUT_B, OUTPUT>>(
    recipe: RECIPE,
    private val firstInputHandler: HTInputHandler<INPUT_A>,
    private val secondInputHandler: HTInputHandler<INPUT_B>,
    private val outputHandler: HTOutputHandler<OUTPUT>,
    private val amountGetter: (RECIPE, INPUT_A, INPUT_B) -> Pair<Int, Int>,
) : HTCompletedRecipe<RECIPE>(recipe) {
    private val output: OUTPUT = recipe.apply(firstInputHandler.getStack(), secondInputHandler.getStack())

    override fun canComplete(): Boolean = outputHandler.canInsert(output)

    override fun complete() {
        outputHandler.insert(output)
        amountGetter(recipe, firstInputHandler.getStack(), secondInputHandler.getStack()).let { (first: Int, second: Int) ->
            firstInputHandler.consume(first)
            secondInputHandler.consume(second)
        }
    }
}
