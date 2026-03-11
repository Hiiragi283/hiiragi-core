package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.fixedFraction
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.apache.commons.lang3.math.Fraction

/**
 * レシピの処理を行う抽象クラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
class HTRecipeHandler<INPUT : Any, RECIPE : Any> private constructor(
    private val inputFactory: (ServerLevel, BlockPos) -> INPUT?,
    private val recipeFinder: (INPUT, ServerLevel) -> RECIPE?,
    private val maxProgressGetter: (RECIPE) -> Int,
    private val progressGetter: (ServerLevel, BlockPos) -> Int,
    private val canComplete: (ServerLevel, BlockPos, INPUT, RECIPE) -> Boolean,
    private val onComplete: (ServerLevel, BlockPos, INPUT, RECIPE) -> Unit,
) {
    companion object {
        @JvmStatic
        inline fun <INPUT : Any, RECIPE : Any> create(builderAction: Builder<INPUT, RECIPE>.() -> Unit): HTRecipeHandler<INPUT, RECIPE> =
            Builder<INPUT, RECIPE>().apply(builderAction).build()
    }

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
        true -> fixedFraction(progress, maxProgress, true)
        false -> Fraction.ZERO
    }

    private var shouldCheck: Boolean = true

    fun createListener(listener: HTContentListener): HTContentListener = HTContentListener {
        shouldCheck = true
        listener.onContentsChanged()
    }

    fun tick(level: ServerLevel, pos: BlockPos): Boolean {
        if (!shouldCheck) return false
        // インプットに一致するレシピを探索する
        val input: INPUT = inputFactory(level, pos) ?: return run {
            shouldCheck = false
            updateProgress(-1)
            false
        }
        val recipe: RECIPE = recipeFinder(input, level) ?: return run {
            shouldCheck = false
            updateProgress(-1)
            false
        }
        val maxProgress: Int = maxProgressGetter(recipe)
        // レシピの最大進捗量を更新する
        if (this.maxProgress != maxProgress) {
            updateProgress(maxProgress)
        }
        // 進捗を更新する
        if (progress < maxProgress) {
            progress += progressGetter(level, pos)
        }
        // アウトプットに完成品を搬出できるか判定する
        if (progress >= maxProgress && canComplete(level, pos, input, recipe)) {
            progress -= maxProgress
            // レシピを実行する
            onComplete(level, pos, input, recipe)
        }
        return true
    }

    private fun updateProgress(maxProgress: Int) {
        this.maxProgress = maxProgress
        progress = 0
    }

    class Builder<INPUT : Any, RECIPE : Any> {
        /**
         * 指定された引数から，入力を取得します。
         * @return 入力を生成できない場合は`null`
         */
        lateinit var inputFactory: (ServerLevel, BlockPos) -> INPUT?

        /**
         * 指定された引数に一致するレシピを取得します。
         * @return 一致するレシピがない場合は`null`
         */
        lateinit var recipeFinder: (INPUT, ServerLevel) -> RECIPE?

        /**
         * 指定された引数から，レシピの最大進捗量を取得します。
         */
        lateinit var maxProgressGetter: (RECIPE) -> Int

        /**
         * 進捗を取得します。
         */
        lateinit var progressGetter: (ServerLevel, BlockPos) -> Int

        /**
         * 指定された引数から，レシピ処理を完了できるかどうか判定します。
         * @return 完了できる場合は`true`, それ以外の場合は`false`
         */
        lateinit var canComplete: (ServerLevel, BlockPos, INPUT, RECIPE) -> Boolean

        /**
         * 指定された引数から，レシピ処理を実行します。
         */
        lateinit var onComplete: (ServerLevel, BlockPos, INPUT, RECIPE) -> Unit

        fun build(): HTRecipeHandler<INPUT, RECIPE> =
            HTRecipeHandler(inputFactory, recipeFinder, maxProgressGetter, progressGetter, canComplete, onComplete)
    }
}
