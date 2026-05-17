package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.fixedFraction
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.apache.commons.lang3.math.Fraction

/**
 * レシピの処理を行う抽象クラスです。
 * @param T レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
abstract class HTProgressHandler<out T> {
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
     * @return `0..1`の範囲に制限された[Fraction]型の値
     */
    fun getProgress(isActive: Boolean): Fraction = when (isActive) {
        true -> fixedFraction(progress, maxProgress, true)
        false -> Fraction.ZERO
    }

    private var shouldCheck: Boolean = true
    private var canProgress: Boolean = false

    fun createListener(listener: HTContentListener): HTContentListener = HTContentListener {
        shouldCheck = true
        listener.onContentsChanged()
    }

    fun tick(level: ServerLevel, pos: BlockPos): Boolean {
        if (!shouldCheck) return false
        // インプットに一致するレシピを探索する
        val recipe: T = findRecipe(level, pos) ?: return run {
            shouldCheck = false
            updateProgress(-1)
            false
        }
        // アウトプットに完成品を搬出できるか判定する
        if (!canProgress) {
            if (canComplete(level, pos, recipe)) {
                canProgress = true
            } else {
                return false
            }
        }
        // レシピの最大進捗量を更新する
        val maxProgress: Int = getMaxProgress(recipe)
        if (this.maxProgress != maxProgress) {
            updateProgress(maxProgress)
        }
        // 進捗を更新する
        if (progress < maxProgress) {
            progress += getProgress(level, pos)
        }
        // 進捗が最大量を超えたらレシピを実行する
        if (progress >= maxProgress) {
            progress -= maxProgress
            canProgress = false
            onComplete(level, pos, recipe)
        }
        return true
    }

    protected abstract fun findRecipe(level: ServerLevel, pos: BlockPos): T?

    protected abstract fun canComplete(level: ServerLevel, pos: BlockPos, recipe: @UnsafeVariance T): Boolean

    protected abstract fun getMaxProgress(recipe: @UnsafeVariance T): Int

    protected abstract fun getProgress(level: ServerLevel, pos: BlockPos): Int

    protected abstract fun onComplete(level: ServerLevel, pos: BlockPos, recipe: @UnsafeVariance T)

    private fun updateProgress(maxProgress: Int) {
        this.maxProgress = maxProgress
        progress = 0
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

        fun build(): HTProgressHandler<T> = object : HTProgressHandler<T>() {
            override fun findRecipe(level: ServerLevel, pos: BlockPos): T? = recipeFinder(level, pos)

            override fun canComplete(level: ServerLevel, pos: BlockPos, recipe: T): Boolean = canComplete.invoke(level, pos, recipe)

            override fun getMaxProgress(recipe: T): Int = maxProgressGetter(recipe)

            override fun getProgress(level: ServerLevel, pos: BlockPos): Int = progressGetter(level, pos)

            override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: T) {
                onComplete.invoke(level, pos, recipe)
            }
        }
    }
}
