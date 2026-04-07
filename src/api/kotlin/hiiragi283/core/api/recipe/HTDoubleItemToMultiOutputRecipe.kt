package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput

/**
 * [HTDoubleRecipeInput]を受け取る[HTMultiOutputRecipe]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
interface HTDoubleItemToMultiOutputRecipe : HTMultiOutputRecipe<HTDoubleRecipeInput> {
    fun getBaseAmount(input: HTDoubleRecipeInput): Int

    fun getAdditionAmount(input: HTDoubleRecipeInput): Int

    //    Serializable    //

    /**
     * シリアライズ可能な[HTItemToMultiOutputRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.15.0
     */
    interface Serializable :
        HTDoubleItemToMultiOutputRecipe,
        HTMultiOutputRecipe.Serializable<HTDoubleRecipeInput>
}
