package hiiragi283.core.api.util

import hiiragi283.core.api.function.identity

sealed class Either<out A, out B> {
    fun isLeft(): Boolean = this is Left<*>

    fun isRight(): Boolean = this is Right<*>

    fun leftOrNull(): A? = when (this) {
        is Left<A> -> this.value
        is Right<B> -> null
    }

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

    fun swap(): Either<B, A> = when (this) {
        is Left<A> -> Right(this.value)
        is Right<B> -> Left(this.value)
    }

    fun toPair(): Pair<A?, B?> = this.fold({ it to null }, { null to it })

    fun toIor(): Ior<A, B> = this.fold({ Ior.Left(it) }, { Ior.Right(it) })

    inline fun <C> map(right: (B) -> C): Either<A, C> = when (this) {
        is Left<A> -> this
        is Right<B> -> right(this.value).let(::Right)
    }

    inline fun <C> mapLeft(left: (A) -> C): Either<C, B> = when (this) {
        is Left<A> -> left(this.value).let(::Left)
        is Right<B> -> this
    }

    inline fun <C> fold(left: (A) -> C, right: (B) -> C): C = when (this) {
        is Left<A> -> left(this.value)
        is Right<B> -> right(this.value)
    }

    data class Left<out A>(val value: A) : Either<A, Nothing>()

    data class Right<out B>(val value: B) : Either<Nothing, B>()
}

//    Extension    //

fun <A> A.left(): Either<A, Nothing> = Either.Left(this)

fun <B> B.right(): Either<Nothing, B> = Either.Right(this)

fun <T> Either<T, T>.unwrap(): T = this.fold(identity(), identity())

inline fun <A, B> Either<A, B>.getOrElse(default: (A) -> B): B = when (this) {
    is Either.Left<A> -> default(this.value)
    is Either.Right<B> -> this.value
}

inline fun <A, B, C> Either<A, B>.flatMap(right: (B) -> Either<A, C>): Either<A, C> = when (this) {
    is Either.Left<A> -> this
    is Either.Right<B> -> right(this.value)
}

inline fun <A, B, C> Either<A, B>.flatMapLeft(left: (A) -> Either<C, B>): Either<C, B> = when (this) {
    is Either.Left<A> -> left(this.value)
    is Either.Right<B> -> this
}

//    DFUEither <-> Either    //

typealias DFUEither<A, B> = com.mojang.datafixers.util.Either<A, B>

val <A, B> DFUEither<A, B>.kotlin: Either<A, B> get() = this.map({ Either.Left(it) }, { Either.Right(it) })

val <A, B> Either<A, B>.java: DFUEither<A, B> get() = this.fold({ DFUEither.left(it) }, { DFUEither.right(it) })
