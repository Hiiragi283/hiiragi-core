package hiiragi283.core.api.collection

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table

/**
 * 二つのキーに対して一つの値で構成されるコレクションを表すクラスです。
 * @param R 行のクラス
 * @param C 列のクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.2.1
 */
@JvmInline
value class ImmutableTable<R : Any, C : Any, V : Any>(private val table: Table<R, C, V>) {
    /**
     * 指定した[row]と[column]が含まれているか判定します。
     */
    fun contains(row: R, column: C): Boolean = table.contains(row, column)

    /**
     * 指定した[row]が含まれているか判定します。
     */
    fun containsRow(row: R): Boolean = table.containsRow(row)

    /**
     * 指定した[column]が含まれているか判定します。
     */
    fun containsColumn(column: C): Boolean = table.containsColumn(column)

    /**
     * 指定した[value]が含まれているか判定します。
     */
    fun containsValue(value: V): Boolean = table.containsValue(value)

    /**
     * 指定した[row]と[column]から対応する値を返します。
     */
    operator fun get(row: R, column: C): V? = table.get(row, column)

    /**
     * このマップに含まれる組の個数を返します。
     */
    val size: Int get() = table.size()

    /**
     * このテーブルが空か判定します。
     */
    val isEmpty: Boolean get() = table.isEmpty

    /**
     * 指定した[row]に対応する列と値のマップを返します。
     */
    fun row(row: R): Map<C, V> = table.row(row)

    /**
     * 指定した[column]に対応する行と値のマップを返します。
     */
    fun column(column: C): Map<R, V> = table.column(column)

    /**
     * このマップに含まれるすべての行のキーの一覧を返します。
     */
    val rowKeys: Set<R> get() = table.rowKeySet()

    /**
     * このマップに含まれるすべての列のキーの一覧を返します。
     */
    val columnKeys: Set<C> get() = table.columnKeySet()

    /**
     * このマップに含まれるすべての値の一覧を返します。
     */
    val values: Collection<V> get() = table.values()

    /**
     * このマップに含まれるすべての組の一覧を返します。
     */
    val entries: Set<Triple<R, C, V>> get() = table.cellSet().map { Triple(it.rowKey, it.columnKey, it.value) }.toSet()

    val rowMap: Map<R, Map<C, V>> get() = table.rowMap()

    val columnMap: Map<C, Map<R, V>> get() = table.columnMap()

    //    Extensions    //

    inline fun forEach(action: (Triple<R, C, V>) -> Unit) {
        entries.forEach(action)
    }

    //    Builder    //

    /**
     * [ImmutableTable]のビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.2.1
     */
    class Builder<R : Any, C : Any, V : Any>(initialRow: Int = 10, initialColumn: Int = 10) {
        private val values: Table<R, C, V> = HashBasedTable.create<R, C, V>(initialRow, initialColumn)

        /**
         * 指定した値を追加します。
         */
        fun put(row: R, column: C, value: V): V? = values.put(row, column, value)

        /**
         * 指定した値を追加します。
         */
        operator fun set(row: R, column: C, value: V) {
            put(row, column, value)
        }

        fun putAll(table: ImmutableTable<out R, out C, out V>): Builder<R, C, V> {
            table.forEach { (r: R, c: C, v: V) -> this.values.put(r, c, v) }
            return this
        }

        /**
         * @see MutableMap.compute
         */
        fun compute(row: R, column: C, mapping: (R, C, V?) -> V?): V? {
            val oldValue: V? = values.get(row, column)
            val newValue: V? = mapping(row, column, oldValue)
            values.put(row, column, newValue)
            return newValue
        }

        /**
         * @see MutableMap.computeIfPresent
         */
        fun computeIfPresent(row: R, column: C, mapping: (R, C, V) -> V?): V? {
            val oldValue: V = values.get(row, column) ?: return null
            val newValue: V? = mapping(row, column, oldValue)
            values.put(row, column, newValue)
            return newValue
        }

        /**
         * @see MutableMap.computeIfAbsent
         */
        fun computeIfAbsent(row: R, column: C, mapping: (R, C) -> V): V {
            val oldValue: V? = values.get(row, column)
            if (oldValue == null) {
                val newValue: V = mapping(row, column)
                values.put(row, column, newValue)
                return newValue
            } else {
                return oldValue
            }
        }

        /**
         * 新しい[ImmutableTable]のインスタンスを返します。
         */
        fun build(): ImmutableTable<R, C, V> = when {
            values.isEmpty -> immutableTableOf()
            else -> ImmutableTable(values)
        }
    }
}
