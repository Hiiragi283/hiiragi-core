package hiiragi283.core.api.collection

/**
 * この[HTTable][this]を[HTTableLike]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun <R, C, V> HTTable<R, C, V>.toLike(): HTTableLike<R, C, V> = object : HTTableLike<R, C, V> {
    override val isEmpty: Boolean get() = this@toLike.isEmpty

    override fun get(row: R, column: C): V? = this@toLike[row, column]

    override fun row(row: R): HTMapLike<C, V> = this@toLike.row(row).toLike()

    override fun column(column: C): HTMapLike<R, V> = this@toLike.column(column).toLike()

    override val rowKeys: Set<R> get() = this@toLike.rowKeys
    override val columnKeys: Set<C> get() = this@toLike.columnKeys

    override fun iterator(): Iterator<Triple<R, C, V>> = this@toLike.entries.iterator()
}
