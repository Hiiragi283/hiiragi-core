package hiiragi283.core.api.text

import com.mojang.datafixers.util.Either
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import java.util.Optional

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 */
fun <T> HTTranslation.toTextResult(): HTTextResult<T> = HTTextResult.error(this.translate())

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param args テキストの引数
 */
fun <T> HTTranslation.toTextResult(vararg args: Any?): HTTextResult<T> = HTTextResult.error(this.translate(*args))

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param color [Component]の色
 */
fun <T> HTTranslation.toTextResult(color: ChatFormatting): HTTextResult<T> = HTTextResult.error(this.translateColored(color))

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param color [Component]の色
 * @param args テキストの引数
 */
fun <T> HTTranslation.toTextResult(color: ChatFormatting, vararg args: Any?): HTTextResult<T> =
    HTTextResult.error(this.translateColored(color, *args))

fun <T : Any> Optional<T>.toTextResult(error: () -> HTTextResult<T>): HTTextResult<T> =
    this.map(HTTextResult.Companion::success).orElseGet(error)

fun <T : Any> Optional<T>.toTextResult(error: HTTranslation): HTTextResult<T> = this.toTextResult(error::toTextResult)

fun HTTextResult<out Component>.unwrap(): Component = Either.unwrap(this.contents)
