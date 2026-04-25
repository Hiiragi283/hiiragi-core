package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.recipe.ingredient.HTIngredient
import java.util.Optional

/**
 * レシピの入力スロットを表すインターフェースです。
 * @param INPUT 材料となるリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.inputs.IInputHandler
 */
interface HTInputHandler<INPUT : HTIngredient<*>> : HTAmountInputHandler {
    /**
     * 指定した[材料][ingredient]から消費される数量を取得します。
     * @return [ingredient]が要求する数量
     */
    fun getMatchingAmount(ingredient: INPUT): Int

    /**
     * 指定した[材料][ingredient]から中身を消費します。
     */
    fun consume(ingredient: INPUT?) {
        ingredient?.let(::getMatchingAmount)?.let(::consume)
    }

    /**
     * 指定した[材料][ingredient]から中身を消費します。
     */
    fun consume(ingredient: Optional<out INPUT>) {
        ingredient.map(::consume)
    }
}
