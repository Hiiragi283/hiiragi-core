@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.util

import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.toText
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import org.slf4j.Logger

typealias HTTextResult<T> = Either<ErrorText, T>

fun HTTextResult(value: String): HTTextResult<Nothing> = ErrorText(value).left()

inline fun <T> T?.toTextResult(message: () -> String): HTTextResult<T> {
    contract {
        callsInPlace(message, InvocationKind.AT_MOST_ONCE)
    }
    return this?.right() ?: HTTextResult(message())
}

fun <T> HTTextResult<T>.printError(logger: Logger): HTTextResult<T> = this.onLeft { logger.error(it.value) }

fun <T> HTTextResult<T>.getOrThrow(): T = this.getOrElse { error(it.value) }

@JvmInline
value class ErrorText(val value: String) : HTHasText {
    override fun getText(): Text = value.toText()
}
