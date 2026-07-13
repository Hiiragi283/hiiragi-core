@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.util

import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.toText
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import org.slf4j.Logger

/**
 * [ErrorText]または[T]を保持する[Either]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
typealias HTTextResult<T> = Either<ErrorText, T>

/**
 * 指定した[value]から[HTTextResult]を作成します。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun HTTextResult(value: String): HTTextResult<Nothing> = ErrorText(value).left()

/**
 * [HTTextResult]に変換します。
 * @param T 値のクラス
 * @param message エラーメッセージを提供するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <T> T?.toTextResult(message: () -> String): HTTextResult<T> {
    contract {
        callsInPlace(message, InvocationKind.AT_MOST_ONCE)
    }
    return this?.right() ?: HTTextResult(message())
}

/**
 * エラーメッセージがある場合，それをログに出力します。
 * @param logger ログの出力先
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T> HTTextResult<T>.printError(logger: Logger): HTTextResult<T> = this.onLeft { logger.error(it.value) }

/**
 * 指定した[HTTextResult][this]から値を取得します。
 * @throws IllegalStateException [ErrorText]を保持している場合
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun <T> HTTextResult<T>.getOrThrow(): T = this.getOrElse { error(it.value) }

/**
 * エラーメッセージを保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@JvmInline
value class ErrorText(val value: String) : HTHasText {
    override fun getText(): Text = value.toText()
}
