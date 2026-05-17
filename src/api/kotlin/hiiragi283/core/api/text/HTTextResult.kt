package hiiragi283.core.api.text

import hiiragi283.core.api.HTDefaultColor
import java.util.Optional

/**
 * エラーを[テキスト][Text]で保持するクラスです。
 * @param T 成功時の結果のクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
sealed class HTTextResult<out T> {
    companion object {
        /**
         * 指定した[value]から[HTTextResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun <T> success(value: T): HTTextResult<T> = Success(value)

        /**
         * 指定した[message]から[HTTextResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun <T> failure(message: Text): HTTextResult<T> = Failure(message)
    }

    /**
     * 保持している値を返します。
     * @return 値がない場合は`null`
     */
    fun getOrNull(): T? = when (this) {
        is Failure -> null
        is Success<T> -> this.value
    }

    /**
     * 保持しているエラーを返します。
     * @return 値がある場合は`null`
     */
    fun getError(): Text? = when (this) {
        is Failure -> this.message
        is Success<T> -> null
    }

    inline fun onSucceeded(action: (T) -> Unit): HTTextResult<T> {
        if (this is Success<T>) {
            action(this.value)
        }
        return this
    }

    inline fun onFailure(action: (Text) -> Unit): HTTextResult<T> {
        if (this is Failure) {
            action(this.message)
        }
        return this
    }

    /**
     * 保持している値を変換します。
     * @param R 戻り値のクラス
     * @param transform 値を[R]に変換するブロック
     * @return 新しい[HTTextResult]のインスタンス
     */
    inline fun <R> map(transform: (T) -> R): HTTextResult<R> = when (this) {
        is Failure -> this
        is Success<T> -> this.value.let(transform).let(::success)
    }

    /**
     * 保持している値を変換します。
     * @param R 戻り値のクラス
     * @param transform 値を[R]の[HTTextResult]に変換するブロック
     * @return 新しい[HTTextResult]のインスタンス
     */
    inline fun <R> flatMap(transform: (T) -> HTTextResult<R>): HTTextResult<R> = when (this) {
        is Failure -> this
        is Success<T> -> this.value.let(transform)
    }

    /**
     * 保持している値を変換します。
     * @param R 戻り値のクラス
     * @param success 値を[R]に変換するブロック
     * @param failure エラーを[R]に変換するブロック
     * @return 変換された値
     */
    inline fun <R> fold(success: (T) -> R, failure: (Text) -> R): R = when (this) {
        is Failure -> this.message.let(failure)
        is Success<T> -> this.value.let(success)
    }

    data class Success<out T>(val value: T) : HTTextResult<T>()

    data class Failure(val message: Text) : HTTextResult<Nothing>()
}

//    Extensions    //

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(): HTTextResult<T> = HTTextResult.failure(this.translate())

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param args テキストの引数
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(vararg args: Any?): HTTextResult<T> = HTTextResult.failure(this.translate(*args))

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param color テキストの色
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(color: HTDefaultColor): HTTextResult<T> = HTTextResult.failure(this.translateColored(color))

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param color テキストの色
 * @param args テキストの引数
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(color: HTDefaultColor, vararg args: Any?): HTTextResult<T> = HTTextResult.failure(this.translateColored(color, *args))

/**
 * この[Optional][this]を[HTTextResult]に変換します。
 * @param error エラー時の[HTTextResult]
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T : Any> Optional<T>.toTextResult(error: () -> HTTextResult<T>): HTTextResult<T> = this.map(HTTextResult.Companion::success).orElseGet(error)

/**
 * この[Optional][this]を[HTTextResult]に変換します。
 * @param error エラーの[テキスト][Text]
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T : Any> Optional<T>.toTextResult(error: HTTranslation): HTTextResult<T> = this.toTextResult(error::toTextResult)
