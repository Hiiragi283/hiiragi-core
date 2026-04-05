package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import net.minecraft.core.HolderLookup
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
     * @param registries レジストリへのアクセス
     */
    fun assembleExtraItem(input: INPUT, registries: HolderLookup.Provider): ItemStack

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
