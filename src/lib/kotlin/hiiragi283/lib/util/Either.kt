package hiiragi283.lib.util

sealed class Either<out A, out B> {
    fun isLeft(): Boolean = this is Left<*>

    fun isRight(): Boolean = this is Right<*>

    fun leftOrNull(): A? = when (this) {
        is Left -> this.value
        is Right -> null
    }

    fun getOrNull(): B? = when (this) {
        is Left -> null
        is Right -> this.value
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
        is Left -> Right(this.value)
        is Right -> Left(this.value)
    }

    fun toPair(): Pair<A?, B?> = this.fold({ it to null }, { null to it })

    fun toIor(): Ior<A, B> = this.fold({ Ior.Left(it) }, { Ior.Right(it) })

    inline fun <C> map(right: (B) -> C): Either<A, C> = when (this) {
        is Left -> this
        is Right -> right(this.value).let(::Right)
    }

    inline fun <C> mapLeft(left: (A) -> C): Either<C, B> = when (this) {
        is Left -> left(this.value).let(::Left)
        is Right -> this
    }

    inline fun <C> fold(left: (A) -> C, right: (B) -> C): C = when (this) {
        is Left -> left(this.value)
        is Right -> right(this.value)
    }

    data class Left<out A>(val value: A) : Either<A, Nothing>()

    data class Right<out B>(val value: B) : Either<Nothing, B>()
}

//    Extension    //

fun <A> A.left(): Either<A, Nothing> = Either.Left(this)

fun <B> B.right(): Either<Nothing, B> = Either.Right(this)

fun <T> Either<T, T>.unwrap(): T = this.fold(identity(), identity())

inline fun <A, B> Either<A, B>.getOrElse(default: (A) -> B): B = when (this) {
    is Either.Left -> default(this.value)
    is Either.Right -> this.value
}

inline fun <A, B, C> Either<A, B>.flatMap(right: (B) -> Either<A, C>): Either<A, C> = when (this) {
    is Either.Left -> this
    is Either.Right -> right(this.value)
}

inline fun <A, B, C> Either<A, B>.flatMapLeft(left: (A) -> Either<C, B>): Either<C, B> = when (this) {
    is Either.Left -> left(this.value)
    is Either.Right -> this
}
