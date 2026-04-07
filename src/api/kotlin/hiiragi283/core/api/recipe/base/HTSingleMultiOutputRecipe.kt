package hiiragi283.core.api.recipe.base

import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * [SingleRecipeInput]を受け取る[HTMultiOutputRecipe]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
interface HTSingleMultiOutputRecipe :
    HTMultiOutputRecipe<SingleRecipeInput>,
    HTSingleItemRecipe {
    //    Serializable    //

    /**
     * シリアライズ可能な[HTSingleMultiOutputRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.15.0
     */
    interface Serializable :
        HTSingleMultiOutputRecipe,
        HTMultiOutputRecipe.Serializable<SingleRecipeInput>
}
