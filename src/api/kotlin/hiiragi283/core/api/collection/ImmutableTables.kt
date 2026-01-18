package hiiragi283.core.api.collection

import com.google.common.collect.ImmutableTable as GoogleTable

fun <R : Any, C : Any, V : Any> immutableTableOf(): ImmutableTable<R, C, V> = ImmutableTable(GoogleTable.of())

inline fun <R : Any, C : Any, V : Any> buildTable(
    initialRow: Int = 10,
    initialColumn: Int = 10,
    builderAction: ImmutableTable.Builder<R, C, V>.() -> Unit,
): ImmutableTable<R, C, V> = ImmutableTable.Builder<R, C, V>(initialRow, initialColumn).apply(builderAction).build()

// toTable
inline fun <T, R : Any, C : Any, V : Any> Iterable<T>.toTable(transform: (T) -> Triple<R, C, V>): ImmutableTable<R, C, V> =
    this.toTable(ImmutableTable.Builder(), transform)

inline fun <T, R : Any, C : Any, V : Any> Iterable<T>.toTable(
    destination: ImmutableTable.Builder<R, C, V>,
    transform: (T) -> Triple<R, C, V>,
): ImmutableTable<R, C, V> {
    for (triple: Triple<R, C, V> in this.map(transform)) {
        destination.put(triple)
    }
    return destination.build()
}

fun <T, R : Any, C : Any, V : Any> Sequence<T>.toTable(transform: (T) -> Triple<R, C, V>): ImmutableTable<R, C, V> =
    this.toTable(ImmutableTable.Builder(), transform)

fun <T, R : Any, C : Any, V : Any> Sequence<T>.toTable(
    destination: ImmutableTable.Builder<R, C, V>,
    transform: (T) -> Triple<R, C, V>,
): ImmutableTable<R, C, V> {
    for (triple: Triple<R, C, V> in this.map(transform)) {
        destination.put(triple)
    }
    return destination.build()
}

inline fun <K, V, R : Any, C : Any, V1 : Any> Map<K, V>.toTable(
    transform: (Map.Entry<K, V>) -> Triple<R, C, V1>,
): ImmutableTable<R, C, V1> = this.toTable(ImmutableTable.Builder(), transform)

inline fun <K, V, R : Any, C : Any, V1 : Any> Map<K, V>.toTable(
    destination: ImmutableTable.Builder<R, C, V1>,
    transform: (Map.Entry<K, V>) -> Triple<R, C, V1>,
): ImmutableTable<R, C, V1> {
    for (triple: Triple<R, C, V1> in this.map(transform)) {
        destination.put(triple)
    }
    return destination.build()
}

// toFlatTable
inline fun <T, R : Any, C : Any, V : Any> Iterable<T>.toFlatTable(transform: (T) -> Iterable<Triple<R, C, V>>): ImmutableTable<R, C, V> =
    this.toFlatTable(ImmutableTable.Builder(), transform)

inline fun <T, R : Any, C : Any, V : Any> Iterable<T>.toFlatTable(
    destination: ImmutableTable.Builder<R, C, V>,
    transform: (T) -> Iterable<Triple<R, C, V>>,
): ImmutableTable<R, C, V> {
    for (triple: Triple<R, C, V> in this.flatMap(transform)) {
        destination.put(triple)
    }
    return destination.build()
}

fun <T, R : Any, C : Any, V : Any> Sequence<T>.toFlatTable(transform: (T) -> Iterable<Triple<R, C, V>>): ImmutableTable<R, C, V> =
    this.toFlatTable(ImmutableTable.Builder(), transform)

fun <T, R : Any, C : Any, V : Any> Sequence<T>.toFlatTable(
    destination: ImmutableTable.Builder<R, C, V>,
    transform: (T) -> Iterable<Triple<R, C, V>>,
): ImmutableTable<R, C, V> {
    for (triple: Triple<R, C, V> in this.flatMap(transform)) {
        destination.put(triple)
    }
    return destination.build()
}

inline fun <K, V, R : Any, C : Any, V1 : Any> Map<K, V>.toFlatTable(
    transform: (Map.Entry<K, V>) -> Iterable<Triple<R, C, V1>>,
): ImmutableTable<R, C, V1> = this.toFlatTable(ImmutableTable.Builder(), transform)

inline fun <K, V, R : Any, C : Any, V1 : Any> Map<K, V>.toFlatTable(
    destination: ImmutableTable.Builder<R, C, V1>,
    transform: (Map.Entry<K, V>) -> Iterable<Triple<R, C, V1>>,
): ImmutableTable<R, C, V1> {
    for (triple: Triple<R, C, V1> in this.flatMap(transform)) {
        destination.put(triple)
    }
    return destination.build()
}

inline fun <K : Any, V : Any, R : Any, C : Any, V1 : Any> ImmutableMultiMap<K, V>.toFlatTable(
    transform: (Map.Entry<K, Collection<V>>) -> Iterable<Triple<R, C, V1>>,
): ImmutableTable<R, C, V1> = this.toFlatTable(ImmutableTable.Builder(), transform)

inline fun <K : Any, V : Any, R : Any, C : Any, V1 : Any> ImmutableMultiMap<K, V>.toFlatTable(
    destination: ImmutableTable.Builder<R, C, V1>,
    transform: (Map.Entry<K, Collection<V>>) -> Iterable<Triple<R, C, V1>>,
): ImmutableTable<R, C, V1> {
    for (triple: Triple<R, C, V1> in this.map.flatMap(transform)) {
        destination.put(triple)
    }
    return destination.build()
}

// filter
inline fun <R : Any, C : Any, V : Any> ImmutableTable<R, C, V>.filter(predicate: (Triple<R, C, V>) -> Boolean): ImmutableTable<R, C, V> =
    this.filterTo(ImmutableTable.Builder(), predicate)

inline fun <R : Any, C : Any, V : Any> ImmutableTable<R, C, V>.filterTo(
    destination: ImmutableTable.Builder<R, C, V>,
    predicate: (Triple<R, C, V>) -> Boolean,
): ImmutableTable<R, C, V> {
    this.forEach { triple: Triple<R, C, V> ->
        if (predicate(triple)) {
            destination.put(triple)
        }
    }
    return destination.build()
}

inline fun <R : Any, C : Any, V : Any> ImmutableTable<R, C, V>.filterNot(predicate: (Triple<R, C, V>) -> Boolean): ImmutableTable<R, C, V> =
    this.filterNotTo(ImmutableTable.Builder(), predicate)

inline fun <R : Any, C : Any, V : Any> ImmutableTable<R, C, V>.filterNotTo(
    destination: ImmutableTable.Builder<R, C, V>,
    predicate: (Triple<R, C, V>) -> Boolean,
): ImmutableTable<R, C, V> {
    this.forEach { triple: Triple<R, C, V> ->
        if (!predicate(triple)) {
            destination.put(triple)
        }
    }
    return destination.build()
}
