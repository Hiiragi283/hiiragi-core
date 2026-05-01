package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.recipe.ingredient.HTIngredient
import java.util.Optional

/**
 * レシピの入力スロットを表すインターフェースです。
 * @param STACK 入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.inputs.IInputHandler
 */
interface HTInputHandler<STACK : Any> : HTAmountInputHandler {
    fun getStack(): STACK

    /**
     * 指定した[材料][ingredient]から中身を消費します。
     */
    fun consume(ingredient: HTIngredient<STACK>)

    /**
     * 指定した[材料][ingredient]から中身を消費します。
     */
    fun consume(ingredient: Optional<out HTIngredient<STACK>>) {
        ingredient.map(::consume)
    }
}
