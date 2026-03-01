package hiiragi283.core.api.util

import java.util.Optional

/**
 * 空の[Optional]を取得します。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <T : Any> emptyOptional(): Optional<T> = Optional.empty<T>()

/**
 * この[インスタンス][this]を[Optional]で包みます。
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <T : Any> T?.wrapOptional(): Optional<T> = Optional.ofNullable(this)

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
inline fun <T : Any> Optional<T>.onPresent(action: (T) -> Unit): Optional<T> {
    if (this.isPresent) {
        action(this.get())
    }
    return this
}
