package hiiragi283.core.api.collection

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table

/**
 * Google Guavaの[Table]に基づいた[HTTable.Mutable]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
class HTHashTable<R, C, out V> : HTTable.Mutable<R, C, V> {
    private val internalTable: Table<R, C, V>

    constructor() : this(10, 10)

    constructor(initialRow: Int, initialColumn: Int) {
        this.internalTable = HashBasedTable.create(initialRow, initialColumn)
    }

    constructor(internalTable: Table<R, C, V>) {
        this.internalTable = internalTable
    }

    constructor(table: HTTable<R, C, V>) : this() {
        for ((row: R, column: C, value: V) in table.entries) {
            this.internalTable.put(row, column, value)
        }
    }

    override fun put(row: R, column: C, value: @UnsafeVariance V): V? = internalTable.put(row, column, value)

    override fun remove(row: R, column: C): V? = internalTable.remove(row, column)

    override fun clear() {
        internalTable.clear()
    }

    //    HTTable    //

    override fun contains(row: R, column: C): Boolean = internalTable.contains(row, column)

    override fun containsRow(row: R): Boolean = internalTable.containsRow(row)

    override fun containsColumn(column: C): Boolean = internalTable.containsColumn(column)

    override fun containsValue(value: @UnsafeVariance V): Boolean = internalTable.containsValue(value)

    override fun get(row: R, column: C): V? = internalTable.get(row, column)

    override val size: Int get() = internalTable.size()
    override val isEmpty: Boolean get() = internalTable.isEmpty

    override fun row(row: R): Map<C, V> = internalTable.row(row)

    override fun column(column: C): Map<R, V> = internalTable.column(column)

    override val rowKeys: Set<R> get() = internalTable.rowKeySet()
    override val columnKeys: Set<C> get() = internalTable.columnKeySet()
    override val values: Collection<V> get() = internalTable.values()
    override val entries: Set<Triple<R, C, V>> = CellSet()
    override val rowMap: Map<R, Map<C, V>> get() = internalTable.rowMap()
    override val columnMap: Map<C, Map<R, V>> get() = internalTable.columnMap()

    private inner class CellSet : AbstractSet<Triple<R, C, V>>() {
        override val size: Int
            get() = internalTable.cellSet().size

        override fun iterator(): Iterator<Triple<R, C, V>> = internalTable
            .cellSet()
            .map { cell: Table.Cell<R, C, V> -> Triple(cell.rowKey, cell.columnKey, cell.value) }
            .iterator()
    }
}
