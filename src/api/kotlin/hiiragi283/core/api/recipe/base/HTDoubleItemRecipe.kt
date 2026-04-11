package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput

/**
 * 二つのアイテムを一つのアイテムに変換するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
interface HTDoubleItemRecipe : HTProcessingRecipe<HTDoubleRecipeInput> {
    fun getBaseAmount(input: HTDoubleRecipeInput): Int

    fun getAdditionAmount(input: HTDoubleRecipeInput): Int

    //    Serializable    //

    /**
     * シリアライズ可能な[HTDoubleItemRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.15.0
     */
    interface Serializable :
        HTDoubleItemRecipe,
        HTSerializableRecipe<HTDoubleRecipeInput>
}
