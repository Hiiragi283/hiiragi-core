package hiiragi283.core.common.recipe.base

import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * [SingleRecipeInput]を受け取る[HTChancedRecipe]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
interface HTItemToChancedRecipe : HTChancedRecipe<SingleRecipeInput> {
    fun getRequiredAmount(input: SingleRecipeInput): Int

    //    Serializable    //

    /**
     * シリアライズ可能な[HTItemToChancedRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Serializable :
        HTItemToChancedRecipe,
        HTChancedRecipe.Serializable<SingleRecipeInput>
}
