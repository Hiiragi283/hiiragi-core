package hiiragi283.core.api.util

sealed interface Option<out T> {
    companion object {
        @JvmStatic
        fun <T> fromNullable(value: T?): Option<T> = if (value == null) None else Some(value)

        @JvmStatic
        operator fun <T> invoke(value: T): Option<T> = Some(value)

        @JvmStatic
        fun <T> none(): Option<T> = None as Option<T>
    }

    val isSome: Boolean get() = this is Some<T>
    val isNone: Boolean get() = this is None

    // filter
    fun filter(predicate: (T) -> Boolean): Option<T> = flatMap { if (predicate(it)) Some(it) else None }

    // map
    fun <R> map(transform: (T) -> R): Option<R> = when (this) {
        None -> None
        is Some<T> -> this.value.let(transform).let(::Some)
    }

    fun <R> flatMap(transform: (T) -> Option<R>): Option<R> = when (this) {
        None -> None
        is Some<T> -> this.value.let(transform)
    }

    fun <R> fold(ifEmpty: () -> R, ifSome: (T) -> R): R = when (this) {
        None -> ifEmpty()
        is Some<T> -> this.value.let(ifSome)
    }

    // collection
    fun toList(): List<T> = fold(::emptyList) { listOf(it) }

    fun toSequence(): Sequence<T> = fold(::emptySequence) { sequenceOf(it) }

    //     Some    //

    @JvmInline
    value class Some<T>(val value: T) : Option<T>

    //     None    //

    data object None : Option<Nothing>
}
