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
        /**
         * 指定した[value]から[HTTextResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun <T> success(value: T): HTTextResult<T> = HTTextResult(Either.right(value))

        /**
         * 指定した[message]から[HTTextResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun <T> error(message: Component): HTTextResult<T> = HTTextResult(Either.left(message))
    }

    /**
     * 保持している値を返します。
     * @return [Optional]に包まれた値
     */
    fun value(): Optional<T> = contents.right()

    /**
     * 保持しているエラーを返します。
     * @return [Optional]に包まれた値
     */
    fun message(): Optional<Component> = contents.left()

    /**
     * 保持している値を変換します。
     * @param R 戻り値のクラス
     * @param transform 値を[R]に変換するブロック
     * @return 新しい[HTTextResult]のインスタンス
     */
    fun <R> map(transform: (T) -> R): HTTextResult<R> = HTTextResult(contents.mapRight(transform))

    /**
     * 保持している値を変換します。
     * @param R 戻り値のクラス
     * @param success 値を[R]に変換するブロック
     * @param error エラーを[R]に変換するブロック
     * @return 変換された値
     */
    fun <R> mapOrElse(success: (T) -> R, error: (Component) -> R): R = contents.map(error, success)

    /**
     * 保持している値を変換します。
     * @param R 戻り値のクラス
     * @param transform 値を[R]の[HTTextResult]に変換するブロック
     * @return 新しい[HTTextResult]のインスタンス
     */
    fun <R> flatMap(transform: (T) -> HTTextResult<R>): HTTextResult<R> = contents.map({ HTTextResult(Either.left(it)) }, { transform(it) })
}
