package hiiragi283.core.api.util

import hiiragi283.core.api.function.identity

/**
 * [A]または[B]の値のみを保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
sealed class Either<out A, out B> {
    /**
     * このインスタンスが[Left]であるか判定します。
     * @return [Left]の場合は`true`，それ以外の場合は`false`
     */
    fun isLeft(): Boolean = this is Left<*>

    /**
     * このインスタンスが[Right]であるか判定します。
     * @return [Right]の場合は`true`，それ以外の場合は`false`
     */
    fun isRight(): Boolean = this is Right<*>

    /**
     * 左側の値を取得します。
     * @return [Right]の場合は`null`
     */
    fun leftOrNull(): A? = when (this) {
        is Left<A> -> this.value
        is Right<B> -> null
    }

    /**
     * 右側の値を取得します。
     * @return [Left]の場合は`null`
     */
    fun getOrNull(): B? = when (this) {
        is Left<A> -> null
        is Right<B> -> this.value
    }

    inline fun onLeft(action: (A) -> Unit): Either<A, B> {
        if (this is Left<A>) {
            action(this.value)
        }
        return this
    }

    inline fun onRight(action: (B) -> Unit): Either<A, B> {
        if (this is Right<B>) {
            action(this.value)
        }
        return this
    }

    /**
     * 左右の値を入れ替えます。
     * @return 値が入れ替わった[Either]のインスタンス
     */
    fun swap(): Either<B, A> = when (this) {
        is Left<A> -> Right(this.value)
        is Right<B> -> Left(this.value)
    }

    /**
     * 保持している値を[Pair]に展開します。
     * @return 展開された[Pair]のインスタンス
     */
    fun toPair(): Pair<A?, B?> = this.fold({ it to null }, { null to it })

    /**
     * 保持している値を[Ior]に展開します。
     * @return 展開された[Ior]のインスタンス
     */
    fun toIor(): Ior<A, B> = this.fold({ Ior.Left(it) }, { Ior.Right(it) })

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param right このインスタンスが[Right]の場合の変換ブロック
     * @return 右側の値が変換された[Either]
     */
    inline fun <C> map(right: (B) -> C): Either<A, C> = when (this) {
        is Left<A> -> this
        is Right<B> -> right(this.value).let(::Right)
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left このインスタンスが[Left]の場合の変換ブロック
     * @return 左側の値が変換された[Either]
     */
    inline fun <C> mapLeft(left: (A) -> C): Either<C, B> = when (this) {
        is Left<A> -> left(this.value).let(::Left)
        is Right<B> -> this
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left このインスタンスが[Left]の場合の変換ブロック
     * @param right このインスタンスが[Right]の場合の変換ブロック
     * @return 変換された値
     */
    inline fun <C> fold(left: (A) -> C, right: (B) -> C): C = when (this) {
        is Left<A> -> left(this.value)
        is Right<B> -> right(this.value)
    }

    /**
     * [A]だけを保持する[Either]の実装クラスです。
     */
    data class Left<out A>(val value: A) : Either<A, Nothing>()

    /**
     * [B]だけを保持する[Either]の実装クラスです。
     */
    data class Right<out B>(val value: B) : Either<Nothing, B>()
}

//    Extension    //

/**
 * 指定した[A][this]を[Either.Left]で包みます。
 */
fun <A> A.left(): Either<A, Nothing> = Either.Left(this)

/**
 * 指定した[A][this]を[Either.Right]で包みます。
 */
fun <B> B.right(): Either<Nothing, B> = Either.Right(this)

/**
 * 保持している値を取り出します。
 */
fun <T> Either<T, T>.unwrap(): T = this.fold(identity(), identity())

/**
 * 保持している値を取得します。
 * @return 指定した[Either][this]が[Either.Left]の場合は[default]の戻り値
 */
inline fun <A, B> Either<A, B>.getOrElse(default: (A) -> B): B = when (this) {
    is Either.Left<A> -> default(this.value)
    is Either.Right<B> -> this.value
}

/**
 * 保持している値を変換します。
 * @param C 変換後のクラス
 * @param right このインスタンスが[Either.Right]の場合の変換ブロック
 * @return 変換された値を保持する[Either]
 */
inline fun <A, B, C> Either<A, B>.flatMap(right: (B) -> Either<A, C>): Either<A, C> = when (this) {
    is Either.Left<A> -> this
    is Either.Right<B> -> right(this.value)
}

/**
 * 保持している値を変換します。
 * @param C 変換後のクラス
 * @param left このインスタンスが[Either.Left]の場合の変換ブロック
 * @return 変換された値を保持する[Either]
 */
inline fun <A, B, C> Either<A, B>.flatMapLeft(left: (A) -> Either<C, B>): Either<C, B> = when (this) {
    is Either.Left<A> -> left(this.value)
    is Either.Right<B> -> this
}

//    DFUEither <-> Either    //

/**
 * DataFixerUpper由来のEitherのエイリアス
 */
typealias DFUEither<A, B> = com.mojang.datafixers.util.Either<A, B>

val <A, B> DFUEither<A, B>.kotlin: Either<A, B> get() = this.map({ Either.Left(it) }, { Either.Right(it) })

val <A, B> Either<A, B>.java: DFUEither<A, B> get() = this.fold({ DFUEither.left(it) }, { DFUEither.right(it) })
