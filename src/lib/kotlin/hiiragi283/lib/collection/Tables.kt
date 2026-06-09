@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.collection

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * @see emptyMap
 */
@Suppress("UNCHECKED_CAST")
fun <R, C, V> emptyTableOf(): Table<R, C, V> = EmptyTable as Table<R, C, V>

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
inline fun <R, C, V> buildTable(builderAction: Table.Builder<R, C, V>.() -> Unit): Table<R, C, V> {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return PairMapTable.Builder<R, C, V>().apply(builderAction).build()
}

/**
 * @see Map.forEach
 */
inline fun <R, C, V> Table<R, C, V>.forEach(action: (Triple<R, C, V>) -> Unit) {
    this.entries.forEach(action)
}

fun <R, C, V> Table<R, C, V>.asSequence(): Sequence<Triple<R, C, V>> = this.entries.asSequence()
