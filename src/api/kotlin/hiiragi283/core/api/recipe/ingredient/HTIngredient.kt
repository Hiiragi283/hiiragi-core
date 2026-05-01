package hiiragi283.core.api.recipe.ingredient

import java.util.function.Predicate

/**
 * レシピの材料を表すインターフェースです。
 * @param STACK 判定の対象となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 * @see HTItemIngredient
 * @see HTFluidIngredient
 * @see mekanism.api.recipes.ingredients.InputIngredient
 */
interface HTIngredient<STACK : Any> : Predicate<STACK> {
    /**
     * 指定した[stack]が条件を満たしているか判定します。
     */
    override fun test(stack: STACK): Boolean

    /**
     * 指定した[stack]が数量を除いて条件を満たしているか判定します。
     */
    fun testOnlyType(stack: STACK): Boolean

    fun getRequiredAmount(stack: STACK): Int

    fun getPreviewStacks(): List<STACK>
}
