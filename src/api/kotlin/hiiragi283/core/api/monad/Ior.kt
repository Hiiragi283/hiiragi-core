package hiiragi283.core.api.monad

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.function.identity

/**
 * [A]と[B]の両方の値または片方だけを保持するクラス。
 */
sealed class Ior<A, B> {
    /**
     * このインスタンスが[Left]であるかどうか判定します。
     * @return [Left]の場合は`true`，それ以外の場合は`false`
     */
    fun isLeft(): Boolean = this is Left<A, B>

    /**
     * このインスタンスが[Right]であるかどうか判定します。
     * @return [Right]の場合は`true`，それ以外の場合は`false`
     */
    fun isRight(): Boolean = this is Right<A, B>

    /**
     * このインスタンスが[Both]であるかどうか判定します。
     * @return [Both]の場合は`true`，それ以外の場合は`false`
     */
    fun isBoth(): Boolean = this is Both<A, B>

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left このインスタンスが[Left]の場合の変換ブロック
     * @param right このインスタンスが[Right]の場合の変換ブロック
     * @param both このインスタンスが[Both]の場合の変換ブロック
     * @return 変換された値
     */
    inline fun <C> fold(left: (A) -> C, right: (B) -> C, both: (A, B) -> C): C = when (this) {
        is Both<A, B> -> both(leftValue, rightValue)
        is Left<A, B> -> left(value)
        is Right<A, B> -> right(value)
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left このインスタンスが[Left]の場合の変換ブロック
     * @param right このインスタンスが[Right]または[Both]の場合の変換ブロック
     * @return 変換された値
     */
    inline fun <C> map(left: (A) -> C, right: (B) -> C): C = when (this) {
        is Both<A, B> -> right(rightValue)
        is Left<A, B> -> left(value)
        is Right<A, B> -> right(value)
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param right このインスタンスが[Right]または[Both]の場合の変換ブロック
     * @return 変換された[Ior]のインスタンス
     */
    inline fun <C> mapRight(right: (B) -> C): Ior<A, C> = when (this) {
        is Both<A, B> -> Both(leftValue, right(rightValue))
        is Left<A, B> -> Left(value)
        is Right<A, B> -> Right(right(value))
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left このインスタンスが[Left]または[Both]の場合の変換ブロック
     * @return 変換された[Ior]のインスタンス
     */
    inline fun <C> mapLeft(left: (A) -> C): Ior<C, B> = when (this) {
        is Both<A, B> -> Both(left(leftValue), rightValue)
        is Left<A, B> -> Left(left(value))
        is Right<A, B> -> Right(value)
    }

    /**
     * 保持している値を入れ替えます。
     * @return 値が入れ替わった[Ior]のインスタンス
     */
    fun swap(): Ior<B, A> = fold(
        { Right(it) },
        { Left(it) },
        { left: A, right: B -> Both(right, left) },
    )

    /**
     * 保持している値を[Either]に展開します。
     * @return 展開された[Either]のインスタンス
     */
    fun unwrap(): Either<Either<A, B>, Pair<A, B>> = fold(
        { Either.left(Either.left(it)) },
        { Either.left(Either.right(it)) },
        { left: A, right: B -> Either.right(left to right) },
    )

    /**
     * 保持している値を[Pair]に展開します。
     * @return 展開された[Pair]のインスタンス
     */
    fun toPair(): Pair<A?, B?> = fold(
        { it to null },
        { null to it },
        { left: A, right: B -> left to right },
    )

    /**
     * [右側][B]の値を取得します。
     * @return このインスタンスが[Left]の場合は`null`
     */
    fun getRight(): B? = fold(
        { null },
        identity(),
        { _: A, right: B -> right },
    )

    /**
     * [左側][A]の値を取得します。
     * @return このインスタンスが[Right]の場合は`null`
     */
    fun getLeft(): A? = fold(
        identity(),
        { null },
        { left: A, _: B -> left },
    )

    /**
     * [A]だけを保持する[Ior]の実装クラス。
     */
    data class Left<A, B>(val value: A) : Ior<A, B>()

    /**
     * [B]だけを保持する[Ior]の実装クラス。
     */
    data class Right<A, B>(val value: B) : Ior<A, B>()

    /**
     * [A]と[B]の両方を保持する[Ior]の実装クラス。
     */
    data class Both<A, B>(val leftValue: A, val rightValue: B) : Ior<A, B>()
}
