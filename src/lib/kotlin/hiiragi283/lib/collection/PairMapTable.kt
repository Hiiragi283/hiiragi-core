package hiiragi283.lib.collection

@JvmInline
value class PairMapTable<R, C, out V>(private val map: Map<Pair<R, C>, V>) : Table<R, C, V> {
    override fun contains(row: R, column: C): Boolean = (row to column) in map

    override fun containsRow(row: R): Boolean = map.any { (key: Pair<R, C>, _) -> key.first == row }

    override fun containsColumn(column: C): Boolean = map.any { (key: Pair<R, C>, _) -> key.second == column }

    override fun containsValue(value: @UnsafeVariance V): Boolean = map.containsValue(value)

    override fun get(row: R, column: C): V? = map[row to column]

    override val size: Int get() = map.size
    override val isEmpty: Boolean get() = map.isEmpty()

    override fun row(row: R): Map<C, V> = map.filterKeys { (rowIn: R, _) -> rowIn == row }.mapKeys { (key: Pair<R, C>, _) -> key.second }

    override fun column(column: C): Map<R, V> = map.filterKeys { (_, columnIn: C) -> columnIn == column }.mapKeys { (key: Pair<R, C>, _) -> key.first }

    override val rowKeys: Set<R> get() = map.keys.mapTo(mutableSetOf()) { it.first }
    override val columnKeys: Set<C> get() = map.keys.mapTo(mutableSetOf()) { it.second }
    override val values: Collection<V> get() = map.values
    override val entries: Set<Triple<R, C, V>> get() = map.entries.mapTo(mutableSetOf()) { (key: Pair<R, C>, value: V) -> Triple(key.first, key.second, value) }
    override val rowMap: Map<R, Map<C, V>> get() = rowKeys.associateWith(::row)
    override val columnMap: Map<C, Map<R, V>> get() = columnKeys.associateWith(::column)
}

@JvmInline
value class MutablePairMapTable<R, C, out V>(private val map: MutableMap<Pair<R, C>, V>) : MutableTable<R, C, V> {
    constructor() : this(mutableMapOf())

    constructor(other: Table<R, C, V>) : this() {
        this.putAll(other)
    }

    override fun put(row: R, column: C, value: @UnsafeVariance V): V? = map.put(row to column, value)

    override fun remove(row: R, column: C): V? = map.remove(row to column)

    override fun clear() {
        map.clear()
    }

    override fun contains(row: R, column: C): Boolean = (row to column) in map

    override fun containsRow(row: R): Boolean = map.any { (key: Pair<R, C>, _) -> key.first == row }

    override fun containsColumn(column: C): Boolean = map.any { (key: Pair<R, C>, _) -> key.second == column }

    override fun containsValue(value: @UnsafeVariance V): Boolean = map.containsValue(value)

    override fun get(row: R, column: C): V? = map[row to column]

    override val size: Int get() = map.size
    override val isEmpty: Boolean get() = map.isEmpty()

    override fun row(row: R): Map<C, V> = map.filterKeys { (rowIn: R, _) -> rowIn == row }.mapKeys { (key: Pair<R, C>, _) -> key.second }

    override fun column(column: C): Map<R, V> = map.filterKeys { (_, columnIn: C) -> columnIn == column }.mapKeys { (key: Pair<R, C>, _) -> key.first }

    override val rowKeys: Set<R> get() = map.keys.mapTo(mutableSetOf()) { it.first }
    override val columnKeys: Set<C> get() = map.keys.mapTo(mutableSetOf()) { it.second }
    override val values: Collection<V> get() = map.values
    override val entries: Set<Triple<R, C, V>> get() = map.entries.mapTo(mutableSetOf()) { (key: Pair<R, C>, value: V) -> Triple(key.first, key.second, value) }
    override val rowMap: Map<R, Map<C, V>> get() = rowKeys.associateWith(::row)
    override val columnMap: Map<C, Map<R, V>> get() = columnKeys.associateWith(::column)
}
