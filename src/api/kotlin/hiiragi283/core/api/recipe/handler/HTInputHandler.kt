package hiiragi283.core.api.recipe.handler

/**
 * レシピの入力スロットを表すインターフェースです。
 * @param STACK 入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.inputs.IInputHandler
 */
interface HTInputHandler<STACK : Any> {
    fun getStack(): STACK

    fun consume(amount: Int)

    /**
     * @since 21.1.0
     */
    fun consume(stack: STACK)
}
