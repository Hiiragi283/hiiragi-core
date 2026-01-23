package hiiragi283.core.api.collection

import hiiragi283.core.api.HTBuilderMarker

/**
 * @see emptyMap
 */
@Suppress("UNCHECKED_CAST")
fun <R, C, V> emptyTableOf(): HTTable<R, C, V> = EmptyTable as HTTable<R, C, V>

/**
 * @see mapOf
 */
fun <R, C, V> tableOf(vararg triples: Triple<R, C, V>): HTTable<R, C, V> = when {
    triples.isNotEmpty() -> triples.toTable(HTHashTable())
    else -> emptyTableOf()
}

/**
 * @see mutableMapOf
 */
fun <R, C, V> mutableTableOf(): HTTable.Mutable<R, C, V> = HTHashTable()

/**
 * @see mutableMapOf
 */
fun <R, C, V> mutableTableOf(vararg triples: Triple<R, C, V>): HTTable.Mutable<R, C, V> =
    HTHashTable<R, C, V>(triples.size, triples.size).apply { putAll(triples) }

/**
 * @see EmptyMap
 */
private data object EmptyTable : HTTable<Nothing, Nothing, Nothing> {
    override fun contains(row: Nothing, column: Nothing): Boolean = false

    override fun containsRow(row: Nothing): Boolean = false

    override fun containsColumn(column: Nothing): Boolean = false

    override fun containsValue(value: Nothing): Boolean = false

    override fun get(row: Nothing, column: Nothing): Nothing? = null

    override val size: Int = 0
    override val isEmpty: Boolean = true

    override fun row(row: Nothing): Map<Nothing, Nothing> = emptyMap()

    override fun column(column: Nothing): Map<Nothing, Nothing> = emptyMap()

    override val rowKeys: Set<Nothing> = emptySet()
    override val columnKeys: Set<Nothing> = emptySet()
    override val values: Collection<Nothing> = emptySet()
    override val entries: Set<Triple<Nothing, Nothing, Nothing>> = emptySet()
    override val rowMap: Map<Nothing, Map<Nothing, Nothing>> = emptyMap()
    override val columnMap: Map<Nothing, Map<Nothing, Nothing>> = emptyMap()
}

/**
 * @see buildMap
 */
@HTBuilderMarker
inline fun <R, C, V> buildTable(
    initialRow: Int = 10,
    initialColumn: Int = 10,
    builderAction: HTTable.Mutable<R, C, V>.() -> Unit,
): HTTable<R, C, V> = HTHashTable<R, C, V>(initialRow, initialColumn).apply(builderAction)

/**
 * @see Map.forEach
 */
inline fun <R, C, V> HTTable<R, C, V>.forEach(action: (Triple<R, C, V>) -> Unit) {
    this.entries.forEach(action)
}

// toTable

/**
 * @see Map.toMap
 */
fun <R, C, V> HTTable<R, C, V>.toTable(): HTTable<R, C, V> = when (this.size) {
    0 -> emptyTableOf()
    1 -> tableOf(this.entries.first())
    else -> this.toMutableTable()
}

/**
 * @see Map.toMutableMap
 */
fun <R, C, V> HTTable<R, C, V>.toMutableTable(): HTTable.Mutable<R, C, V> = HTHashTable(this)

/**
 * @see Iterable.toMap
 */
fun <R, C, V> Iterable<Triple<R, C, V>>.toTable(): HTTable<R, C, V> {
    if (this is Collection<Triple<R, C, V>>) {
        return when (this.size) {
            0 -> emptyTableOf()
            1 -> tableOf(if (this is List) this[0] else iterator().next())
            else -> this.toTable(HTHashTable(this.size, this.size))
        }
    }
    return this.toTable(HTHashTable())
}

/**
 * @see Iterable.toMap
 */
fun <R, C, V, T : HTTable.Mutable<in R, in C, in V>> Iterable<Triple<R, C, V>>.toTable(destination: T): T =
    destination.apply { putAll(this@toTable) }

/**
 * @see Array.toMap
 */
fun <R, C, V, T : HTTable.Mutable<in R, in C, in V>> Array<out Triple<R, C, V>>.toTable(destination: T): T =
    destination.apply { putAll(this@toTable) }

// with transform
inline fun <K, V, R, C, V1> Map<K, V>.toTable(transform: (Map.Entry<K, V>) -> Triple<R, C, V1>): HTTable<R, C, V1> =
    this.toTable(HTHashTable(), transform)

inline fun <K, V, R, C, V1, T : HTTable.Mutable<R, C, V1>> Map<K, V>.toTable(
    destination: T,
    transform: (Map.Entry<K, V>) -> Triple<R, C, V1>,
): T {
    for (triple: Triple<R, C, V1> in this.map(transform)) {
        destination.put(triple)
    }
    return destination
}

// toFlatTable
inline fun <T, R, C, V> Iterable<T>.toFlatTable(transform: (T) -> Iterable<Triple<R, C, V>>): HTTable<R, C, V> =
    this.toFlatTable(HTHashTable(), transform)

inline fun <T, R, C, V, T1 : HTTable.Mutable<R, C, V>> Iterable<T>.toFlatTable(
    destination: T1,
    transform: (T) -> Iterable<Triple<R, C, V>>,
): T1 {
    for (triple: Triple<R, C, V> in this.flatMap(transform)) {
        destination.put(triple)
    }
    return destination
}

fun <T, R, C, V> Sequence<T>.toFlatTable(transform: (T) -> Iterable<Triple<R, C, V>>): HTTable<R, C, V> =
    this.toFlatTable(HTHashTable(), transform)

fun <T, R, C, V, T1 : HTTable.Mutable<R, C, V>> Sequence<T>.toFlatTable(destination: T1, transform: (T) -> Iterable<Triple<R, C, V>>): T1 {
    for (triple: Triple<R, C, V> in this.flatMap(transform)) {
        destination.put(triple)
    }
    return destination
}

inline fun <K, V, R, C, V1> Map<K, V>.toFlatTable(transform: (Map.Entry<K, V>) -> Iterable<Triple<R, C, V1>>): HTTable<R, C, V1> =
    this.toFlatTable(HTHashTable(), transform)

inline fun <K, V, R, C, V1, T : HTTable.Mutable<R, C, V1>> Map<K, V>.toFlatTable(
    destination: T,
    transform: (Map.Entry<K, V>) -> Iterable<Triple<R, C, V1>>,
): T {
    for (triple: Triple<R, C, V1> in this.flatMap(transform)) {
        destination.put(triple)
    }
    return destination
}

// filter
inline fun <R, C, V> HTTable<R, C, V>.filter(predicate: (Triple<R, C, V>) -> Boolean): HTTable<R, C, V> =
    this.filterTo(HTHashTable(), predicate)

inline fun <R, C, V, T : HTTable.Mutable<R, C, V>> HTTable<R, C, V>.filterTo(destination: T, predicate: (Triple<R, C, V>) -> Boolean): T {
    this.forEach { triple: Triple<R, C, V> ->
        if (predicate(triple)) {
            destination.put(triple)
        }
    }
    return destination
}

inline fun <R, C, V> HTTable<R, C, V>.filterNot(predicate: (Triple<R, C, V>) -> Boolean): HTTable<R, C, V> =
    this.filterNotTo(HTHashTable(), predicate)

inline fun <R, C, V, T : HTTable.Mutable<R, C, V>> HTTable<R, C, V>.filterNotTo(
    destination: T,
    predicate: (Triple<R, C, V>) -> Boolean,
): T {
    this.forEach { triple: Triple<R, C, V> ->
        if (!predicate(triple)) {
            destination.put(triple)
        }
    }
    return destination
}
