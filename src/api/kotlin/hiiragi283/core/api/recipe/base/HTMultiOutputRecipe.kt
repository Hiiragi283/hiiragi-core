package hiiragi283.core.api.recipe.base

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 複数の完成品をもつ[HTProcessingRecipe]の拡張インターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
interface HTMultiOutputRecipe<INPUT : RecipeInput> : HTProcessingRecipe<INPUT> {
    /**
     * 指定された引数から完成品を作成します。
     * @param input レシピの入力
     */
    fun assembleItems(input: INPUT, preview: Boolean): List<ItemStack>

    @Deprecated("Use 'assembleItems(INPUT, Boolean)' instead")
    override fun assemble(input: INPUT, preview: Boolean): ItemStack = assembleItems(input, preview).firstOrNull() ?: ItemStack.EMPTY

    //    Serializable    //

    /**
     * シリアライズ可能な[HTMultiOutputRecipe]の拡張インターフェースです。
     * @param INPUT レシピの入力となるクラス
     * @author Hiiragi Tsubasa
     * @since 0.15.0
     */
    interface Serializable<INPUT : RecipeInput> :
        HTMultiOutputRecipe<INPUT>,
        HTProcessingRecipe.Serializable<INPUT> {
        @Suppress("DEPRECATION")
        override fun assemble(input: INPUT, preview: Boolean): ItemStack = super<HTMultiOutputRecipe>.assemble(input, preview)
    }
}
