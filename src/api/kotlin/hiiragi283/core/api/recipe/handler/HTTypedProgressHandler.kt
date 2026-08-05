package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.HTContentListener
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel

/**
 * レシピの処理を行う抽象クラスです。
 * @param T レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
abstract class HTTypedProgressHandler<out T> : HTProgressHandler {
    override var progress: Int = 0
    override var maxProgress: Int = 0

    private var shouldCheck: Boolean = true
    private var canProgress: Boolean = false

    override fun createListener(listener: HTContentListener): HTContentListener = HTContentListener {
        shouldCheck = true
        listener.onContentsChanged()
    }

    final override fun tick(level: ServerLevel, pos: BlockPos): Boolean {
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
}
