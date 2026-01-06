package hiiragi283.core.api.text

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.monad.unwrap
import net.minecraft.network.chat.Component
import java.util.Optional

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(): HTTextResult<T> = HTTextResult.error(this.translate())

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param args テキストの引数
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(vararg args: Any?): HTTextResult<T> = HTTextResult.error(this.translate(*args))

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param color テキストの色
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(color: HTDefaultColor): HTTextResult<T> = HTTextResult.error(this.translateColored(color))

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param color テキストの色
 * @param args テキストの引数
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(color: HTDefaultColor, vararg args: Any?): HTTextResult<T> =
    HTTextResult.error(this.translateColored(color, *args))

/**
 * この[Optional][this]を[HTTextResult]に変換します。
 * @param error エラー時の[HTTextResult]
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T : Any> Optional<T>.toTextResult(error: () -> HTTextResult<T>): HTTextResult<T> =
    this.map(HTTextResult.Companion::success).orElseGet(error)

/**
 * この[Optional][this]を[HTTextResult]に変換します。
 * @param error エラーの[テキスト][Component]
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T : Any> Optional<T>.toTextResult(error: HTTranslation): HTTextResult<T> = this.toTextResult(error::toTextResult)

/**
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun HTTextResult<out Component>.unwrap(): Component = this.contents.unwrap()
