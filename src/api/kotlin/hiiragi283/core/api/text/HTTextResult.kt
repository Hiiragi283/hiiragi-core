package hiiragi283.core.api.text

import com.mojang.datafixers.util.Either
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

/**
 * エラーを[テキスト][Component]で保持するモナド
 * @param T 成功時の結果のクラス
 */
typealias HTTextResult<T> = Either<T, Component>

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 */
fun <T> HTTranslation.toTextResult(): HTTextResult<T> = Either.right<T, Component>(this.translate())

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param args テキストの引数
 */
fun <T> HTTranslation.toTextResult(vararg args: Any?): HTTextResult<T> = Either.right<T, Component>(this.translate(*args))

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param color [Component]の色
 */
fun <T> HTTranslation.toTextResult(color: ChatFormatting): HTTextResult<T> = Either.right<T, Component>(this.translateColored(color))

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param color [Component]の色
 * @param args テキストの引数
 */
fun <T> HTTranslation.toTextResult(color: ChatFormatting, vararg args: Any?): HTTextResult<T> =
    Either.right<T, Component>(this.translateColored(color, *args))
