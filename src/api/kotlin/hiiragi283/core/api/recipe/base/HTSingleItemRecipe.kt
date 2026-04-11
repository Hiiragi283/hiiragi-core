package hiiragi283.core.api.recipe.base

import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 一つのアイテムを一つのアイテムに変換するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
interface HTSingleItemRecipe : HTProcessingRecipe<SingleRecipeInput> {
    fun getRequiredAmount(input: SingleRecipeInput): Int

    //    Serializable    //

    /**
     * シリアライズ可能な[HTSingleItemRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Serializable :
        HTSingleItemRecipe,
        HTProcessingRecipe.Serializable<SingleRecipeInput>
}
