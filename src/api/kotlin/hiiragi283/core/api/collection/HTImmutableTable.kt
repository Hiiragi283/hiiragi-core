package hiiragi283.core.api.collection

/**
 * 不変な[HTTable]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.2.0
 * @see com.google.common.collect.ArrayTable
 */
class HTImmutableTable<R : Any, C : Any, V : Any> private constructor(
    override val rowMap: Map<R, Map<C, V>>,
    override val columnMap: Map<C, Map<R, V>>,
) : HTTable<R, C, V> {
    companion object {
        /**
         * [HTTable]を作成します。
         * @param R 行のクラス
         * @param C 列のクラス
         * @param V 値のクラス
         * @param entries テーブルの組の一覧
         * @return 新しい[HTTable]のインスタンス
         */
        @JvmStatic
        fun <R : Any, C : Any, V : Any> create(entries: Iterable<Triple<R, C, V>>): HTTable<R, C, V> {
            val rowMap: MutableMap<R, MutableMap<C, V>> = mutableMapOf()
            val columnMap: MutableMap<C, MutableMap<R, V>> = mutableMapOf()
            for ((row: R, column: C, value: V) in entries) {
                rowMap.computeIfAbsent(row) { hashMapOf() }[column] = value
                columnMap.computeIfAbsent(column) { hashMapOf() }[row] = value
            }
            return HTImmutableTable(rowMap, columnMap)
        }

        /**
         * [HTTable]を作成します。
         * @param R 行のクラス
         * @param C 列のクラス
         * @param V 値のクラス
         * @param rowKeys 行の一覧
         * @param columnKeys 列の一覧
         * @param valueFiller 行と列から値を返すブロック
         * @return 新しい[HTTable]のインスタンス
         */
        @JvmStatic
        fun <R : Any, C : Any, V : Any> create(
            rowKeys: Collection<R>,
            columnKeys: Collection<C>,
            valueFiller: (R, C) -> V?,
        ): HTTable<R, C, V> {
            val rowMap: MutableMap<R, MutableMap<C, V>> = mutableMapOf()
            val columnMap: MutableMap<C, MutableMap<R, V>> = mutableMapOf()
            for (row: R in rowKeys) {
                for (column: C in columnKeys) {
                    val value: V = valueFiller(row, column) ?: continue
                    rowMap.computeIfAbsent(row) { hashMapOf() }[column] = value
                    columnMap.computeIfAbsent(column) { hashMapOf() }[row] = value
                }
            }
            return HTImmutableTable(rowMap, columnMap)
        }
    }

    override fun contains(row: R, column: C): Boolean = rowMap[row]?.containsKey(column) ?: false

    override fun containsRow(row: R): Boolean = row in rowMap

    override fun containsColumn(column: C): Boolean = column in columnMap

    override fun containsValue(value: V): Boolean {
        for (row: Map<C, V> in rowMap.values) {
            if (row.containsValue(value)) return true
        }
        return false
    }

    override fun get(row: R, column: C): V? = rowMap[row]?.get(column)

    override fun isEmpty(): Boolean = rowMap.isEmpty() || columnMap.isEmpty()

    override val size: Int = rowMap.size

    override fun row(row: R): Map<C, V> = rowMap[row] ?: emptyMap()

    override fun column(column: C): Map<R, V> = columnMap[column] ?: emptyMap()

    override val rowKeys: Set<R> = rowMap.keys
    override val columnKeys: Set<C> = columnMap.keys
    override val values: Collection<V> = rowKeys.map(::row).flatMap { it.values }
    override val entries: Set<Triple<R, C, V>> =
        rowMap.flatMap { (row: R, map: Map<C, V>) -> map.map { (column: C, value: V) -> Triple(row, column, value) } }.toSet()

    /**
     * [HTImmutableTable.create]で使用されるビルダークラスです。
     * @param R 行のクラス
     * @param C 列のクラス
     * @param V 値のクラス
     * @author Hiiragi Tsubasa
     * @since 0.2.0
     */
    class Builder<R : Any, C : Any, V : Any> {
        private val entries: MutableSet<Triple<R, C, V>> = mutableSetOf()

        /**
         * 指定した値を追加します。
         */
        fun add(row: R, column: C, value: V) {
            entries.add(Triple(row, column, value))
        }

        fun build(): Set<Triple<R, C, V>> = entries
    }
}
