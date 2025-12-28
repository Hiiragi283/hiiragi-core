package hiiragi283.core.api.text

import com.mojang.datafixers.util.Either
import net.minecraft.network.chat.Component
import java.util.Optional

/**
 * エラーを[テキスト][Component]で保持するクラスです。
 * @param T 成功時の結果のクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
@JvmInline
value class HTTextResult<T> private constructor(val contents: Either<Component, T>) {
    companion object {
        @JvmStatic
        fun <T> success(value: T): HTTextResult<T> = HTTextResult(Either.right(value))

        @JvmStatic
        fun <T> error(message: Component): HTTextResult<T> = HTTextResult(Either.left(message))
    }

    fun value(): Optional<T> = contents.right()

    fun message(): Optional<Component> = contents.left()

    fun <R> map(transform: (T) -> R): HTTextResult<R> = HTTextResult(contents.mapRight(transform))

    fun <R> mapOrElse(success: (T) -> R, error: (Component) -> R): R = contents.map(error, success)

    fun <R> flatMap(transform: (T) -> HTTextResult<R>): HTTextResult<R> = contents.map({ HTTextResult(Either.left(it)) }, { transform(it) })

    fun ifPresent(action: (T) -> Unit) {
        contents.ifRight(action)
    }

    fun ifPresentOrElse(success: (T) -> Unit, error: (Component) -> Unit) {
        contents.ifRight(success).ifLeft(error)
    }
}
