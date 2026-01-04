package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.math.fixedFraction
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import org.apache.commons.lang3.math.Fraction

/**
 * レシピの処理を行う抽象クラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
abstract class HTRecipeHandler<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> {
    /**
     * 現在の進捗量
     */
    var progress: Int = 0

    /**
     * 現在の最大進捗量
     */
    var maxProgress: Int = 0

    /**
     * 進捗率を取得します。
     * @param isActive 稼働中かどうかの判定
     * @return `0..1`の範囲に制限された[Fraction]型の値
     */
    fun getProgress(isActive: Boolean): Fraction = when (isActive) {
        true -> fixedFraction(progress, maxProgress)
        false -> Fraction.ZERO
    }

    fun tick(level: ServerLevel, pos: BlockPos): Boolean {
        // インプットに一致するレシピを探索する
        val input: INPUT = createRecipeInput(level, pos) ?: return false
        val recipe: RECIPE = getMatchedRecipe(input, level) ?: return false
        val maxProgress: Int = getMaxProgress(recipe)
        // レシピの最大進捗量を更新する
        if (this.maxProgress != maxProgress) {
            this.maxProgress = maxProgress
            this.progress = 0
        }
        // 進捗を更新する
        if (progress < maxProgress) {
            progress += getProgress(level, pos)
        }
        // アウトプットに完成品を搬出できるか判定する
        if (progress >= maxProgress && canProgressRecipe(level, input, recipe)) {
            progress -= maxProgress
            // レシピを実行する
            completeRecipe(level, pos, input, recipe)
        }
        return true
    }

    /**
     * 指定された引数から，入力を取得します。
     * @return 入力を生成できない場合は`null`
     */
    protected abstract fun createRecipeInput(level: ServerLevel, pos: BlockPos): INPUT?

    /**
     * 指定された[input]と[level]に一致するレシピを取得します。
     * @return 一致するレシピがない場合は`null`
     */
    protected abstract fun getMatchedRecipe(input: INPUT, level: ServerLevel): RECIPE?

    /**
     * 指定された[recipe]から，レシピの最大進捗量を取得します。
     */
    protected abstract fun getMaxProgress(recipe: RECIPE): Int

    /**
     * 進捗を取得します。
     */
    protected abstract fun getProgress(level: ServerLevel, pos: BlockPos): Int

    /**
     * 指定された引数から，レシピ処理を完了できるかどうか判定します。
     * @return 完了できる場合は`true`, それ以外の場合は`false`
     */
    protected abstract fun canProgressRecipe(level: ServerLevel, input: INPUT, recipe: RECIPE): Boolean

    /**
     * 指定された引数から，レシピ処理を実行します。
     */
    protected abstract fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        input: INPUT,
        recipe: RECIPE,
    )
}
