package hiiragi283.core.api.monad

import hiiragi283.core.api.function.identity

/**
 * [A]と[B]の片方だけを保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
sealed class Either<A, B> {
    /**
     * このインスタンスが[Left]であるか判定します。
     * @return [Left]の場合は`true`，それ以外の場合は`false`
     */
    fun isLeft(): Boolean = this is Left<A, B>

    /**
     * このインスタンスが[Right]であるか判定します。
     * @return [Right]の場合は`true`，それ以外の場合は`false`
     */
    fun isRight(): Boolean = this is Right<A, B>

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left このインスタンスが[Left]の場合の変換ブロック
     * @param right このインスタンスが[Right]の場合の変換ブロック
     * @return 変換された値
     */
    inline fun <C> map(left: (A) -> C, right: (B) -> C): C = when (this) {
        is Left<A, B> -> left(value)
        is Right<A, B> -> right(value)
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param right このインスタンスが[Right]の場合の変換ブロック
     * @return 変換された[Either]のインスタンス
     */
    inline fun <C> mapRight(right: (B) -> C): Either<A, C> = when (this) {
        is Left<A, B> -> Left(value)
        is Right<A, B> -> Right(right(value))
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left このインスタンスが[Left]の場合の変換ブロック
     * @return 変換された[Either]のインスタンス
     */
    inline fun <C> mapLeft(left: (A) -> C): Either<C, B> = when (this) {
        is Left<A, B> -> Left(left(value))
        is Right<A, B> -> Right(value)
    }

    /**
     * 保持している値を入れ替えます。
     * @return 値が入れ替わった[Either]のインスタンス
     */
    fun swap(): Either<B, A> = map(::Right, ::Left)

    /**
     * 保持している値を[Pair]に展開します。
     * @return 展開された[Pair]のインスタンス
     */
    fun toPair(): Pair<A?, B?> = getLeft() to getRight()

    /**
     * [右側][B]の値を取得します。
     * @return このインスタンスが[Left]の場合は`null`
     */
    fun getRight(): B? = map({ null }, identity())

    /**
     * [左側][A]の値を取得します。
     * @return このインスタンスが[Right]の場合は`null`
     */
    fun getLeft(): A? = map(identity(), { null })

    /**
     * [A]だけを保持する[Either]の実装クラスです。
     */
    data class Left<A, B>(val value: A) : Either<A, B>()

    /**
     * [B]だけを保持する[Either]の実装クラスです。
     */
    data class Right<A, B>(val value: B) : Either<A, B>()
}
