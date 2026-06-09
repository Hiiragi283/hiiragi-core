@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.util

import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed class Option<out T> {
    companion object {
        @JvmStatic
        fun <T> fromNullable(value: T?): Option<T> = if (value == null) None else Some(value)

        @JvmStatic
        operator fun <T> invoke(value: T): Option<T> = Some(value)
    }

    fun isSome(): Boolean = this is Some<T>

    fun isNone(): Boolean = this is None

    fun getOrNull(): T? = getOrElse { null }

    inline fun onSome(action: (T) -> Unit): Option<T> {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }
        if (this is Some<T>) {
            action(this.value)
        }
        return this
    }

    inline fun onNone(action: () -> Unit): Option<T> {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }
        if (this is None) {
            action()
        }
        return this
    }

    inline fun <R> map(transform: (T) -> R): Option<R> {
        contract {
            callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
        }
        return flatMap { Some(transform(it)) }
    }

    inline fun <R> fold(empty: () -> R, some: (T) -> R): R {
        contract {
            callsInPlace(empty, InvocationKind.AT_MOST_ONCE)
            callsInPlace(some, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is None -> empty()
            is Some<T> -> some(this.value)
        }
    }

    inline fun <R> flatMap(transform: (T) -> Option<R>): Option<R> {
        contract {
            callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is None -> this
            is Some -> transform(this.value)
        }
    }

    inline fun filter(predicate: (T) -> Boolean): Option<T> {
        contract {
            callsInPlace(predicate, InvocationKind.AT_MOST_ONCE)
        }
        return flatMap { if (predicate(it)) Some(it) else None }
    }

    inline fun filterNot(predicate: (T) -> Boolean): Option<T> {
        contract {
            callsInPlace(predicate, InvocationKind.AT_MOST_ONCE)
        }
        return flatMap { if (!predicate(it)) Some(it) else None }
    }

    fun <L> toEither(empty: () -> L): Either<L, T> {
        contract {
            callsInPlace(empty, InvocationKind.AT_MOST_ONCE)
        }
        return fold({ empty().left() }, { it.right() })
    }

    fun toList(): List<T> = fold(::emptyList, ::listOf)

    data class Some<out T>(val value: T) : Option<T>()

    data object None : Option<Nothing>()
}

//    Extension    //

inline fun <T> Option<T>.getOrElse(default: () -> T): T {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Option.None -> default()
        is Option.Some -> this.value
    }
}

fun <T> T?.toOption(): Option<T> = Option.fromNullable(this)

fun <T> T.some(): Option<T> = Option.Some(this)

fun <T> none(): Option<T> = Option.None

fun <K, V> Option<Pair<K, V>>.toMap(): Map<K, V> = this.toList().toMap()

//    Optional <-> Option    //

val <T : Any> Optional<T>.kotlin: Option<T> get() = this.map { it.some() }.orElseGet { none() }

val <T : Any> Option<T>.java: Optional<T> get() = this.fold({ Optional.empty() }, { Optional.of(it) })
