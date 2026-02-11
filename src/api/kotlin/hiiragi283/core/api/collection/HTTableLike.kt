package hiiragi283.core.api.collection

interface HTTableLike<R, C, V> : Iterable<Triple<R, C, V>> {
    val isEmpty: Boolean

    operator fun get(row: R, column: C): V?

    fun row(row: R): HTMapLike<C, V>

    fun column(column: C): HTMapLike<R, V>

    val rowKeys: Set<R>

    val columnKeys: Set<C>
}
