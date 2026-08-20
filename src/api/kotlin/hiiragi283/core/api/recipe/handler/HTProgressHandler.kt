package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.fixedFraction
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.apache.commons.lang3.math.Fraction

/**
 * レシピの処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 21.1.1.0
 */
interface HTProgressHandler {
    /**
     * 現在の進捗量
     */
    var progress: Int

    /**
     * 現在の最大進捗量
     */
    var maxProgress: Int

    /**
     * 進捗率を取得します。
     * @param isActive 稼働中かどうかの判定
     * @return `0..1`の範囲に制限された[Fraction]型の値
     */
    fun getProgress(isActive: Boolean): Fraction = when (isActive) {
        true -> fixedFraction(progress, maxProgress, true)
        false -> Fraction.ZERO
    }

    fun createListener(listener: HTContentListener): HTContentListener = listener

    fun tick(level: ServerLevel, pos: BlockPos): Boolean
}
