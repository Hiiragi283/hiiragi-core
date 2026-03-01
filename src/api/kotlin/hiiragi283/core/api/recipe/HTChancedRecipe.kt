package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import net.minecraft.core.HolderLookup
import net.minecraft.util.RandomSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.LevelAccessor

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
     * @param level レベルへのアクセス
     */
    fun assembleExtraItem(input: INPUT, level: LevelAccessor): ItemStack = assembleExtraItem(input, level.registryAccess(), level.random)

    /**
     * 指定された引数から完成品を作成します。
     * @param input レシピの入力
     * @param registries レジストリへのアクセス
     * @param random 確率を提供する乱数
     */
    fun assembleExtraItem(input: INPUT, registries: HolderLookup.Provider, random: RandomSource): ItemStack =
        assembleExtraItem(input, registries, random.nextFloat())

    /**
     * 指定された引数から完成品を作成します。
     * @param input レシピの入力
     * @param registries レジストリへのアクセス
     * @param chance 現在の確率 (`0f..1f`)
     */
    fun assembleExtraItem(input: INPUT, registries: HolderLookup.Provider, chance: Float): ItemStack

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
