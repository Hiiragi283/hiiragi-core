package hiiragi283.core.api.collection

/**
 * @see AbstractMap
 */
abstract class HTAbstractTable<R, C, V> : HTTable<R, C, V> {
    override fun contains(row: R, column: C): Boolean = get(row, column) != null

    override fun containsRow(row: R): Boolean = row in rowKeys

    override fun containsColumn(column: C): Boolean = column in columnKeys

    override fun containsValue(value: V): Boolean = value in values

    override fun get(row: R, column: C): V? = entries
        .firstOrNull { (rowIn: R, columnIn: C, _: V) -> rowIn == row && columnIn == column }
        ?.third

    override val size: Int get() = entries.size

    override val isEmpty: Boolean get() = entries.isEmpty()

    override fun row(row: R): Map<C, V> = entries
        .filter { (rowIn: R, _, _) -> rowIn == row }
        .associate { (_, column: C, value: V) -> column to value }

    override fun column(column: C): Map<R, V> = entries
        .filter { (_, columnIn: C, _) -> columnIn == column }
        .associate { (row: R, _, value: V) -> row to value }

    override val rowKeys: Set<R> get() = entries.map { it.first }.toSet()

    override val columnKeys: Set<C> get() = entries.map { it.second }.toSet()

    override val values: Collection<V> get() = entries.map { it.third }

    override val rowMap: Map<R, Map<C, V>> get() = rowKeys.associateWith(::row)

    override val columnMap: Map<C, Map<R, V>> get() = columnKeys.associateWith(::column)
}
