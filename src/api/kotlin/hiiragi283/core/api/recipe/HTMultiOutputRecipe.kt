package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import net.minecraft.core.HolderLookup
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
     * @param registries レジストリへのアクセス
     */
    fun assembleItems(input: INPUT, registries: HolderLookup.Provider): List<ItemStack>

    @Deprecated("Use 'assembleItems(INPUT, HolderLookup.Provider)' instead")
    override fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack =
        assembleItems(input, registries).firstOrNull() ?: ItemStack.EMPTY

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
        override fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack = super.assemble(input, registries)
    }
}
