package hiiragi283.core.api.collection

fun <R : Any, C : Any, V : Any> Iterable<Triple<R, C, V>>.toTable(): HTTable<R, C, V> {
    val table: HTTable.Mutable<R, C, V> = HTWrappedTable.Mutable()
    this.forEach(table::put)
    return table
}

inline fun <R : Any, C : Any, V : Any> buildTable(builderAction: HTTable.Mutable<R, C, V>.() -> Unit): HTTable<R, C, V> =
    HTWrappedTable.Mutable<R, C, V>().apply(builderAction)

inline fun <R : Any, C : Any, V : Any> HTTable<R, C, V>.forEach(action: (Triple<R, C, V>) -> Unit) {
    this.entries.forEach(action)
}

fun <R : Any, C : Any, V : Any> HTTable<R, C, V>.asSequence(): Sequence<Triple<R, C, V>> = this.entries.asSequence()
