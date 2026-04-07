package hiiragi283.core.api.recipe

import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * [SingleRecipeInput]を受け取る[HTMultiOutputRecipe]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
interface HTItemToMultiOutputRecipe : HTMultiOutputRecipe<SingleRecipeInput> {
    fun getRequiredAmount(input: SingleRecipeInput): Int

    //    Serializable    //

    /**
     * シリアライズ可能な[HTItemToMultiOutputRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.15.0
     */
    interface Serializable :
        HTItemToMultiOutputRecipe,
        HTMultiOutputRecipe.Serializable<SingleRecipeInput>
}
