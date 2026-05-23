package hiiragi283.core.api.util

import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.toText

typealias HTTextResult<T> = Either<ErrorText, T>

fun HTTextResult(value: String): HTTextResult<Nothing> = ErrorText(value).left()

fun <T> HTTextResult<T>.getOrThrow(): T = this.getOrElse { error(it.value) }

@JvmInline
value class ErrorText(val value: String) : HTHasText {
    override fun getText(): Text = value.toText()
}
