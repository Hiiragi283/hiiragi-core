package hiiragi283.core.api.util

import com.mojang.serialization.DataResult
import java.util.Optional
import java.util.function.Supplier

fun <R : Any> R?.wrapResult(message: Supplier<String>): DataResult<R> = this?.let(DataResult<R>::success) ?: DataResult.error(message)

fun <R : Any> Optional<R>.wrapResult(message: Supplier<String>): DataResult<R> = this.map(DataResult<R>::success).orElseGet {
    DataResult.error(message)
}
