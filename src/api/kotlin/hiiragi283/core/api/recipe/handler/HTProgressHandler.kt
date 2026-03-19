package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.util.fixedFraction
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel

typealias HTRecipeHandler<INPUT, RECIPE> = HTProgressHandler<out HTHandledRecipe<INPUT, RECIPE>>

/**
 * レシピの処理を行う抽象クラスです。
 * @param T レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
class HTProgressHandler<T : Any> private constructor(
    private val recipeFinder: LevelFunction<T?>,
    private val maxProgressGetter: (T) -> Int,
    private val progressGetter: LevelFunction<Int>,
    private val canComplete: BiLevelFunction<T, Boolean>,
    private val onComplete: BiLevelFunction<T, Unit>,
) {
    companion object {
        @JvmStatic
        inline fun <T : Any> create(builderAction: Builder<T>.() -> Unit): HTProgressHandler<T> = Builder<T>().apply(builderAction).build()
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
     * @return `0f..1f`の範囲に制限された[Float]型の値
     */
    fun getProgress(isActive: Boolean): Float = when (isActive) {
        true -> fixedFraction(progress, maxProgress, true)
        false -> 0f
    }

    private var shouldCheck: Boolean = true

    /*fun createListener(listener: HTContentListener): HTContentListener = HTContentListener {
        shouldCheck = true
        listener.onContentsChanged()
    }*/

    fun tick(level: ServerLevel, pos: BlockPos): Boolean {
        if (!shouldCheck) return false
        // インプットに一致するレシピを探索する
        val recipe: T = recipeFinder.apply(level, pos) ?: return run {
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
            progress += progressGetter.apply(level, pos)
        }
        // アウトプットに完成品を搬出できるか判定する
        if (progress >= maxProgress && canComplete.apply(level, pos, recipe)) {
            progress -= maxProgress
            // レシピを実行する
            onComplete.apply(level, pos, recipe)
        }
        return true
    }

    private fun updateProgress(maxProgress: Int) {
        this.maxProgress = maxProgress
        progress = 0
    }

    fun interface LevelFunction<T> {
        fun apply(level: ServerLevel, pos: BlockPos): T
    }

    fun interface BiLevelFunction<T, R> {
        fun apply(level: ServerLevel, pos: BlockPos, value: T): R
    }

    class Builder<T : Any> {
        /**
         * 指定された引数に一致するレシピを取得します。
         * @return 入力を生成できない場合は`null`
         */
        lateinit var recipeFinder: (ServerLevel, BlockPos) -> T?

        /**
         * 指定された引数から，レシピの最大進捗量を取得します。
         */
        lateinit var maxProgressGetter: (T) -> Int

        /**
         * 進捗を取得します。
         */
        lateinit var progressGetter: (ServerLevel, BlockPos) -> Int

        /**
         * 指定された引数から，レシピ処理を完了できるかどうか判定します。
         * @return 完了できる場合は`true`, それ以外の場合は`false`
         */
        lateinit var canComplete: (ServerLevel, BlockPos, T) -> Boolean

        /**
         * 指定された引数から，レシピ処理を実行します。
         */
        lateinit var onComplete: (ServerLevel, BlockPos, T) -> Unit

        fun build(): HTProgressHandler<T> = HTProgressHandler(recipeFinder, maxProgressGetter, progressGetter, canComplete, onComplete)
    }
}
