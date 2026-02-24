package hiiragi283.core.api.text

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.unwrap
import java.util.Optional

/**
 * エラーを[テキスト][Text]で保持するクラスです。
 * @param T 成功時の結果のクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
class HTTextResult<T> private constructor(val contents: Either<Text, T>) {
    companion object {
        /**
         * 指定した[value]から[HTTextResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun <T> success(value: T): HTTextResult<T> = HTTextResult(Either.Right(value))

        /**
         * 指定した[message]から[HTTextResult]のインスタンスを作成します。
         */
        @JvmStatic
        fun <T> error(message: Text): HTTextResult<T> = HTTextResult(Either.Left(message))
    }

    /**
     * 保持している値を返します。
     * @return 値がない場合は`null`
     */
    fun value(): T? = contents.getRight()

    /**
     * 保持している値を返します。
     * @return 値がない場合は[fallback]の戻り値
     * @since 0.10.0
     */
    inline fun valueOrElse(fallback: () -> T): T = value() ?: fallback()

    /**
     * 保持しているエラーを返します。
     * @return 値がある場合は`null`
     */
    fun message(): Text? = contents.getLeft()

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
    fun <R> mapOrElse(success: (T) -> R, error: (Text) -> R): R = contents.map(error, success)

    /**
     * 保持している値を変換します。
     * @param R 戻り値のクラス
     * @param transform 値を[R]の[HTTextResult]に変換するブロック
     * @return 新しい[HTTextResult]のインスタンス
     */
    fun <R> flatMap(transform: (T) -> HTTextResult<R>): HTTextResult<R> = contents.map({ HTTextResult(Either.Left(it)) }, { transform(it) })
}

//    Extensions    //

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(): HTTextResult<T> = HTTextResult.error(this.translate())

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param args テキストの引数
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(vararg args: Any?): HTTextResult<T> = HTTextResult.error(this.translate(*args))

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param color テキストの色
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(color: HTDefaultColor): HTTextResult<T> = HTTextResult.error(this.translateColored(color))

/**
 * この[HTTranslation]を[HTTextResult]に変換します。
 * @param color テキストの色
 * @param args テキストの引数
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T> HTTranslation.toTextResult(color: HTDefaultColor, vararg args: Any?): HTTextResult<T> =
    HTTextResult.error(this.translateColored(color, *args))

/**
 * この[Optional][this]を[HTTextResult]に変換します。
 * @param error エラー時の[HTTextResult]
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T : Any> Optional<T>.toTextResult(error: () -> HTTextResult<T>): HTTextResult<T> =
    this.map(HTTextResult.Companion::success).orElseGet(error)

/**
 * この[Optional][this]を[HTTextResult]に変換します。
 * @param error エラーの[テキスト][Text]
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun <T : Any> Optional<T>.toTextResult(error: HTTranslation): HTTextResult<T> = this.toTextResult(error::toTextResult)

/**
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun HTTextResult<out Text>.unwrap(): Text = this.contents.unwrap()
