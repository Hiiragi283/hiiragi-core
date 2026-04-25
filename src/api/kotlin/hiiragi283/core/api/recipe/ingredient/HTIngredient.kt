package hiiragi283.core.api.recipe.ingredient

import hiiragi283.core.api.storage.resource.HTResourceType
import java.util.function.Predicate

/**
 * レシピの材料を表すインターフェースです。
 * @param RESOURCE 判定の対象となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 * @see HTItemIngredient
 * @see HTFluidIngredient
 * @see mekanism.api.recipes.ingredients.InputIngredient
 */
interface HTIngredient<RESOURCE : HTResourceType> {
    /**
     * 指定した[resource]と[amount]が条件を満たしているか判定します。
     */
    fun test(resource: RESOURCE, amount: Int): Boolean

    fun getRequiredAmount(resource: RESOURCE, amount: Int): Int

    /**
     * @author Hiiragi Tsubasa
     * @since 0.15.3
     */
    interface Stacked<STACK : Any, RESOURCE : HTResourceType> :
        HTIngredient<RESOURCE>,
        Predicate<STACK> {
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
}
