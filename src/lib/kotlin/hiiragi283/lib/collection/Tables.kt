package hiiragi283.lib.collection

/**
 * @see emptyMap
 */
@Suppress("UNCHECKED_CAST")
fun <R, C, V> emptyTableOf(): Table<R, C, V> = EmptyTable as Table<R, C, V>

/**
 * @see mapOf
 */
fun <R, C, V> tableOf(vararg triples: Triple<R, C, V>): Table<R, C, V> = when {
    triples.isNotEmpty() -> triples.toTable(MutablePairMapTable())
    else -> emptyTableOf()
}

/**
 * @see mutableMapOf
 */
fun <R, C, V> mutableTableOf(): MutableTable<R, C, V> = MutablePairMapTable()

/**
 * @see mutableMapOf
 */
fun <R, C, V> mutableTableOf(vararg triples: Triple<R, C, V>): MutableTable<R, C, V> = MutablePairMapTable<R, C, V>().apply { putAll(triples) }

/**
 * @see EmptyMap
 */
private data object EmptyTable : Table<Nothing, Nothing, Nothing> {
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
inline fun <R, C, V> buildTable(builderAction: MutableTable<R, C, V>.() -> Unit): Table<R, C, V> = MutablePairMapTable<R, C, V>().apply(builderAction)

/**
 * @see Map.forEach
 */
inline fun <R, C, V> Table<R, C, V>.forEach(action: (Triple<R, C, V>) -> Unit) {
    this.entries.forEach(action)
}

fun <R, C, V> Table<R, C, V>.asSequence(): Sequence<Triple<R, C, V>> = this.entries.asSequence()

// toTable

/**
 * @see Map.toMap
 */
fun <R, C, V> Table<R, C, V>.toTable(): Table<R, C, V> = when (this.size) {
    0 -> emptyTableOf()
    1 -> tableOf(this.entries.first())
    else -> this.toMutableTable()
}

/**
 * @see toMutableMap
 */
fun <R, C, V> Table<R, C, V>.toMutableTable(): MutableTable<R, C, V> = MutablePairMapTable(this)

/**
 * @see Iterable.toMap
 */
fun <R, C, V> Iterable<Triple<R, C, V>>.toTable(): Table<R, C, V> {
    if (this is Collection<Triple<R, C, V>>) {
        return when (this.size) {
            0 -> emptyTableOf()
            1 -> tableOf(if (this is List) this[0] else iterator().next())
            else -> this.toTable(MutablePairMapTable())
        }
    }
    return this.toTable(MutablePairMapTable())
}

/**
 * @see Iterable.toMap
 */
fun <R, C, V, T : MutableTable<in R, in C, V>> Iterable<Triple<R, C, V>>.toTable(destination: T): T = destination.apply { putAll(this@toTable) }

/**
 * @see Array.toMap
 */
fun <R, C, V, T : MutableTable<in R, in C, V>> Array<out Triple<R, C, V>>.toTable(destination: T): T = destination.apply { putAll(this@toTable) }

// with transform
inline fun <K, V, R, C, V1> Map<K, V>.toTable(transform: (Map.Entry<K, V>) -> Triple<R, C, V1>): Table<R, C, V1> = this.toTable(MutablePairMapTable(), transform)

inline fun <K, V, R, C, V1, T : MutableTable<R, C, V1>> Map<K, V>.toTable(destination: T, transform: (Map.Entry<K, V>) -> Triple<R, C, V1>): T {
    for (triple: Triple<R, C, V1> in this.map(transform)) {
        destination.put(triple)
    }
    return destination
}

// toFlatTable
inline fun <T, R, C, V> Iterable<T>.toFlatTable(transform: (T) -> Iterable<Triple<R, C, V>>): Table<R, C, V> = this.toFlatTable(MutablePairMapTable(), transform)

inline fun <T, R, C, V, T1 : MutableTable<R, C, V>> Iterable<T>.toFlatTable(destination: T1, transform: (T) -> Iterable<Triple<R, C, V>>): T1 {
    for (triple: Triple<R, C, V> in this.flatMap(transform)) {
        destination.put(triple)
    }
    return destination
}

fun <T, R, C, V> Sequence<T>.toFlatTable(transform: (T) -> Iterable<Triple<R, C, V>>): Table<R, C, V> = this.toFlatTable(MutablePairMapTable(), transform)

fun <T, R, C, V, T1 : MutableTable<R, C, V>> Sequence<T>.toFlatTable(destination: T1, transform: (T) -> Iterable<Triple<R, C, V>>): T1 {
    for (triple: Triple<R, C, V> in this.flatMap(transform)) {
        destination.put(triple)
    }
    return destination
}

inline fun <K, V, R, C, V1> Map<K, V>.toFlatTable(transform: (Map.Entry<K, V>) -> Iterable<Triple<R, C, V1>>): Table<R, C, V1> = this.toFlatTable(MutablePairMapTable(), transform)

inline fun <K, V, R, C, V1, T : MutableTable<R, C, V1>> Map<K, V>.toFlatTable(destination: T, transform: (Map.Entry<K, V>) -> Iterable<Triple<R, C, V1>>): T {
    for (triple: Triple<R, C, V1> in this.flatMap(transform)) {
        destination.put(triple)
    }
    return destination
}

// filter

/**
 * @see Sequence.filter
 */
fun <R, C, V> Table<R, C, V>.filter(predicate: (Triple<R, C, V>) -> Boolean): Table<R, C, V> = this.filterTo(MutablePairMapTable(), predicate)

/**
 * @see Sequence.filterTo
 */
inline fun <R, C, V, T : MutableTable<R, C, V>> Table<R, C, V>.filterTo(destination: T, predicate: (Triple<R, C, V>) -> Boolean): T {
    this.forEach { triple: Triple<R, C, V> ->
        if (predicate(triple)) {
            destination.put(triple)
        }
    }
    return destination
}

/**
 * @see Sequence.filterNot
 */
fun <R, C, V> Table<R, C, V>.filterNot(predicate: (Triple<R, C, V>) -> Boolean): Table<R, C, V> = this.filterNotTo(MutablePairMapTable(), predicate)

/**
 * @see Sequence.filterNotTo
 */
inline fun <R, C, V, T : MutableTable<R, C, V>> Table<R, C, V>.filterNotTo(destination: T, predicate: (Triple<R, C, V>) -> Boolean): T {
    this.forEach { triple: Triple<R, C, V> ->
        if (!predicate(triple)) {
            destination.put(triple)
        }
    }
    return destination
}

// associate
inline fun <R, C, V, K, V1> Table<R, C, V>.associate(transform: (Triple<R, C, V>) -> Pair<K, V1>): Map<K, V1> = this.associateTo(linkedMapOf(), transform)

inline fun <R, C, V, K, V1, M : MutableMap<in K, in V1>> Table<R, C, V>.associateTo(destination: M, transform: (Triple<R, C, V>) -> Pair<K, V1>): M {
    this.forEach { triple: Triple<R, C, V> ->
        destination += transform(triple)
    }
    return destination
}

inline fun <R, C, V, K> Table<R, C, V>.associateBy(transform: (Triple<R, C, V>) -> K): Map<K, V> = this.associateByTo(linkedMapOf(), transform)

inline fun <R, C, V, K, M : MutableMap<in K, in V>> Table<R, C, V>.associateByTo(destination: M, transform: (Triple<R, C, V>) -> K): M {
    this.forEach { triple: Triple<R, C, V> ->
        destination[transform(triple)] = triple.third
    }
    return destination
}
