package hiiragi283.core.api.material

import hiiragi283.core.api.collection.ImmutableTable

@JvmInline
value class HTMaterialTable<R : Any, V : Any>(private val table: ImmutableTable<R, HTMaterialKey, V>) {
    operator fun get(row: R, material: HTMaterialLike): V? = table[row, material.asMaterialKey()]

    fun isEmpty(): Boolean = table.isEmpty

    val size: Int get() = table.size

    fun row(row: R): Map<HTMaterialKey, V> = table.row(row)

    fun column(material: HTMaterialLike): Map<R, V> = table.column(material.asMaterialKey())

    val rowKeys: Set<R> get() = table.rowKeys

    val columnKeys: Set<HTMaterialKey> get() = table.columnKeys

    val values: Collection<V> get() = table.values

    val entries: Set<Triple<R, HTMaterialKey, V>> get() = table.entries

    val rowMap: Map<R, Map<HTMaterialKey, V>> get() = table.rowMap

    val columnMap: Map<HTMaterialKey, Map<R, V>> get() = table.columnMap

    inline fun forEach(action: (Triple<R, HTMaterialKey, V>) -> Unit) {
        entries.forEach(action)
    }
}
