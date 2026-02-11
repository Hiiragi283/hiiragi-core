package hiiragi283.core.api.collection

class HTPatchedTable<R, C, V>(val prototype: HTTable<R, C, V>, val patch: HTTable<R, C, V>) : HTTableLike<R, C, V> {
    override val isEmpty: Boolean get() = prototype.isEmpty && patch.isEmpty

    override operator fun get(row: R, column: C): V? = patch[row, column] ?: prototype[row, column]

    override fun row(row: R): HTPatchedMap<C, V> = HTPatchedMap(prototype.row(row), patch.row(row))

    override fun column(column: C): HTPatchedMap<R, V> = HTPatchedMap(prototype.column(column), patch.column(column))

    override val rowKeys: Set<R> get() = prototype.rowKeys.plus(patch.rowKeys)

    override val columnKeys: Set<C> get() = prototype.columnKeys.plus(patch.columnKeys)

    override fun iterator(): Iterator<Triple<R, C, V>> = iterator {
        yieldAll(prototype.entries)
        yieldAll(patch.entries)
    }
}
