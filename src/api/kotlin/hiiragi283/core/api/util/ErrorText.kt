package hiiragi283.core.api.util

import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.toText

/**
 * [ErrorText]または[T]を保持する[Either]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
typealias HTTextResult<T> = Either<ErrorText, T>

/**
 * 指定した[value]から[HTTextResult]を作成します。
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun HTTextResult(value: String): HTTextResult<Nothing> = ErrorText(value).left()

/**
 * 指定した[HTTextResult][this]から値を取得します。
 * @throws IllegalStateException [ErrorText]を保持している場合
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
fun <T> HTTextResult<T>.getOrThrow(): T = this.getOrElse { error(it.value) }

/**
 * エラーメッセージを保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
@JvmInline
value class ErrorText(val value: String) : HTHasText {
    override fun getText(): Text = value.toText()
}
