package hiiragi283.lib.util

import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.toText

typealias HTTextResult<T> = Either<ErrorText, T>

fun HTTextResult(value: String): HTTextResult<Nothing> = ErrorText(value).left()

@JvmInline
value class ErrorText(val value: String) : HTHasText {
    override fun getText(): Text = value.toText()
}
