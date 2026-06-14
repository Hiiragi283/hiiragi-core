@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.util

import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 選択的に値を保持するクラスです。
 *
 * 参照 : [Arrow - Option](https://github.com/arrow-kt/arrow/blob/main/arrow-libs/core/arrow-core/src/commonMain/kotlin/arrow/core/Option.kt)
 * @param T 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed class Option<out T> {
    companion object {
        /**
         * 新しい[Option]のインスタンスを作成します。
         * @return [value]が`null`の場合は[None]，それ以外の場合は[Some]
         */
        @JvmStatic
        fun <T> fromNullable(value: T?): Option<T> = if (value == null) None else Some(value)

        /**
         * 新しい[Option]のインスタンスを作成します。
         */
        @JvmStatic
        operator fun <T> invoke(value: T): Option<T> = Some(value)
    }

    /**
     * 値があるかどうか判定します。
     * @return 値がある場合は`true`
     */
    fun isSome(): Boolean = this is Some<T>

    /**
     * 値がないかどうか判定します。
     * @return 値がない場合は`true`
     */
    fun isNone(): Boolean = this is None

    /**
     * 値を取得します。
     * @return 値がない場合は`null`
     */
    fun getOrNull(): T? = getOrElse { null }

    /**
     * 値がある場合に処理を行います。
     * @param action [Some]の場合に実行されるブロック
     */
    inline fun onSome(action: (T) -> Unit): Option<T> {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }
        if (this is Some<T>) {
            action(this.value)
        }
        return this
    }

    /**
     * 値がない場合に処理を行います。
     * @param action [None]の場合に実行されるブロック
     */
    inline fun onNone(action: () -> Unit): Option<T> {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }
        if (this is None) {
            action()
        }
        return this
    }

    /**
     * 保持している値を変換します。
     * @param R 変換後のクラス
     * @param transform 値を変換するブロック
     */
    inline fun <R> map(transform: (T) -> R): Option<R> {
        contract {
            callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
        }
        return flatMap { Some(transform(it)) }
    }

    /**
     * 保持している値を変換します。
     * @param R 変換後のクラス
     * @param empty 値がない場合に実行されるブロック
     * @param some 値を変換するブロック
     */
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

    /**
     * 保持している値を別の[Option]に変換します。
     * @param R 変換後の値クラス
     * @param transform 値を[Option]に変換するブロック
     */
    inline fun <R> flatMap(transform: (T) -> Option<R>): Option<R> {
        contract {
            callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is None -> this
            is Some -> transform(this.value)
        }
    }

    /**
     * 保持している値を制限します。
     * @param predicate 値を制限するブロック
     * @return [predicate]の戻り値が`false`の場合は[None]
     */
    inline fun filter(predicate: (T) -> Boolean): Option<T> {
        contract {
            callsInPlace(predicate, InvocationKind.AT_MOST_ONCE)
        }
        return flatMap { if (predicate(it)) Some(it) else None }
    }

    /**
     * 保持している値を制限します。
     * @param predicate 値を制限するブロック
     * @return [predicate]の戻り値が`true`の場合は[None]
     */
    inline fun filterNot(predicate: (T) -> Boolean): Option<T> {
        contract {
            callsInPlace(predicate, InvocationKind.AT_MOST_ONCE)
        }
        return flatMap { if (!predicate(it)) Some(it) else None }
    }

    /**
     * [Either]に変換します。
     * @param L 左側の値のクラス
     * @param empty 左側の値を提供するブロック
     * @return 保持している値を右側とする[Either]
     */
    fun <L> toEither(empty: () -> L): Either<L, T> {
        contract {
            callsInPlace(empty, InvocationKind.AT_MOST_ONCE)
        }
        return fold({ empty().left() }, { it.right() })
    }

    /**
     * [List]に変換します。
     */
    fun toList(): List<T> = fold(::emptyList, ::listOf)

    /**
     * 値を保持する[Option]の実装クラスです。
     * @param value 保持している値
     */
    data class Some<out T>(val value: T) : Option<T>()

    /**
     * 値を保持しない[Option]の実装クラスです。
     */
    data object None : Option<Nothing>()
}

//    Extension    //

/**
 * 値を取得します。
 * @return 値がない場合は`default`の戻り値
 */
inline fun <T> Option<T>.getOrElse(default: () -> T): T {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Option.None -> default()
        is Option.Some -> this.value
    }
}

/**
 * [Option]に変換します。
 * @return [this]が`null`の場合は[Option.None]，それ以外の場合は[Option.Some]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T> T?.toOption(): Option<T> = Option.fromNullable(this)

/**
 * [Option.Some]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T> T.some(): Option<T> = Option.Some(this)

/**
 * [Option.None]を[Option]にキャストします。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T> none(): Option<T> = Option.None

/**
 * [Pair]の[Option]を[Map]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <K, V> Option<Pair<K, V>>.toMap(): Map<K, V> = this.toList().toMap()

//    Optional <-> Option    //

/**
 * [Optional]を[Option]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <T : Any> Optional<T>.kotlin: Option<T> get() = this.map { it.some() }.orElseGet { none() }

/**
 * [Option]を[Optional]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <T : Any> Option<T>.java: Optional<T> get() = this.fold({ Optional.empty() }, { Optional.of(it) })
