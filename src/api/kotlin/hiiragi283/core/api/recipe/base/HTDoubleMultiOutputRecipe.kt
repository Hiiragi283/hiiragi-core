package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput

/**
 * [HTDoubleRecipeInput]を受け取る[HTMultiOutputRecipe]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
interface HTDoubleMultiOutputRecipe :
    HTMultiOutputRecipe<HTDoubleRecipeInput>,
    HTDoubleItemRecipe {
    //    Serializable    //

    /**
     * シリアライズ可能な[HTDoubleMultiOutputRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.15.0
     */
    interface Serializable :
        HTDoubleMultiOutputRecipe,
        HTMultiOutputRecipe.Serializable<HTDoubleRecipeInput>
}
