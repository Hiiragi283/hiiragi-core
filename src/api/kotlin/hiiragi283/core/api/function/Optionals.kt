package hiiragi283.core.api.function

import java.util.Optional

fun <T : Any> T?.wrapOptional(): Optional<T> = Optional.ofNullable(this)

inline fun <T : Any> Optional<T>.onPresent(action: (T) -> Unit): Optional<T> {
    if (this.isPresent) {
        action(this.get())
    }
    return this
}
