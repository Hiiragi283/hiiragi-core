package hiiragi283.core.common.recipe.base

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 確率付きの完成品をもつ[HTProcessingRecipe]の拡張インターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
interface HTChancedRecipe<INPUT : RecipeInput> : HTProcessingRecipe<INPUT> {
    /**
     * 指定された引数から完成品を作成します。
     * @param input レシピの入力
     * @param random 確率を提供する乱数
     */
    fun assembleExtraItem(input: INPUT, random: RandomSource): ItemStack = assembleExtraItem(input, random.nextFloat())

    /**
     * 指定された引数から完成品を作成します。
     * @param input レシピの入力
     * @param chance 現在の確率 (`0f..1f`)
     */
    fun assembleExtraItem(input: INPUT, chance: Float): ItemStack

    //    Serializable    //

    /**
     * シリアライズ可能な[HTChancedRecipe]の拡張インターフェースです。
     * @param INPUT レシピの入力となるクラス
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Serializable<INPUT : RecipeInput> :
        HTChancedRecipe<INPUT>,
        HTSerializableRecipe<INPUT>
}
