package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 単一のアイテムを単一のアイテムに変換するレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
interface HTSingleItemRecipe : HTProcessingRecipe<SingleRecipeInput> {
    fun getRequiredAmount(input: SingleRecipeInput): Int

    //    Serializable    //

    /**
     * シリアライズ可能な[HTProcessingRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Serializable :
        HTSingleItemRecipe,
        HTSerializableRecipe<SingleRecipeInput>
}
