package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.storage.resource.HTResourceType
import java.util.Optional

/**
 * レシピの入力スロットを表すインターフェースです。
 * @param RESOURCE 対象となるリソースのクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see mekanism.api.recipes.inputs.IInputHandler
 */
interface HTInputHandler<RESOURCE : HTResourceType<*>> {
    /**
     * 指定した[材料][ingredient]から消費される数量を取得します。
     * @return [ingredient]が要求する数量
     */
    fun getMatchingAmount(ingredient: HTIngredient<*, RESOURCE>): Int

    /**
     * 指定した[材料][ingredient]から中身を消費します。
     */
    fun consume(ingredient: HTIngredient<*, RESOURCE>?) {
        ingredient?.let(::getMatchingAmount)?.let(::consume)
    }

    /**
     * 指定した[材料][ingredient]から中身を消費します。
     */
    fun consume(ingredient: Optional<out HTIngredient<*, RESOURCE>>) {
        ingredient.map(::consume)
    }

    /**
     * 指定した[数量][amount]だけ中身を消費します。
     */
    fun consume(amount: Int)
}
