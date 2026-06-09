package hiiragi283.lib.collection

/**
 * 二つのキーに対して一つの値で構成されるコレクションを表すクラスです。
 * @param R 行のクラス
 * @param C 列のクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
interface Table<R, C, out V> {
    /**
     * 指定した[row]と[column]が含まれているか判定します。
     */
    fun contains(row: R, column: C): Boolean

    /**
     * 指定した[row]が含まれているか判定します。
     */
    fun containsRow(row: R): Boolean

    /**
     * 指定した[column]が含まれているか判定します。
     */
    fun containsColumn(column: C): Boolean

    /**
     * 指定した[value]が含まれているか判定します。
     */
    fun containsValue(value: @UnsafeVariance V): Boolean

    /**
     * 指定した[row]と[column]から対応する値を返します。
     */
    operator fun get(row: R, column: C): V?

    /**
     * このテーブルに含まれる組の個数を返します。
     */
    val size: Int

    /**
     * このテーブルが空か判定します。
     */
    val isEmpty: Boolean

    /**
     * 指定した[row]に対応する列と値のマップを返します。
     */
    fun row(row: R): Map<C, V>

    /**
     * 指定した[column]に対応する行と値のマップを返します。
     */
    fun column(column: C): Map<R, V>

    /**
     * このテーブルに含まれるすべての行のキーの一覧を返します。
     */
    val rowKeys: Set<R>

    /**
     * このテーブルに含まれるすべての列のキーの一覧を返します。
     */
    val columnKeys: Set<C>

    /**
     * このテーブルに含まれるすべての値の一覧を返します。
     */
    val values: Collection<V>

    /**
     * このテーブルに含まれるすべての組の一覧を返します。
     */
    val entries: Set<Triple<R, C, V>>

    val rowMap: Map<R, Map<C, V>>

    val columnMap: Map<C, Map<R, V>>

    interface Builder<R, C, out V> {
        /**
         * 指定した値を追加します。
         */
        fun put(row: R, column: C, value: @UnsafeVariance V): V?

        /**
         * 指定した値を追加します。
         */
        fun put(triple: Triple<R, C, @UnsafeVariance V>): V? = put(triple.first, triple.second, triple.third)

        /**
         * 指定した値を追加します。
         */
        operator fun set(row: R, column: C, value: @UnsafeVariance V) {
            put(row, column, value)
        }

        /**
         * 指定した値を追加します。
         */
        fun putAll(triples: Iterable<Triple<R, C, @UnsafeVariance V>>) {
            triples.forEach(::put)
        }

        /**
         * 指定した値を追加します。
         */
        fun putAll(triples: Sequence<Triple<R, C, @UnsafeVariance V>>) {
            triples.forEach(::put)
        }

        /**
         * 指定した値を追加します。
         */
        fun putAll(triples: Array<out Triple<R, C, @UnsafeVariance V>>) {
            triples.forEach(::put)
        }

        /**
         * ほかのテーブルから値を追加します。
         */
        fun putAll(table: Table<out R, out C, @UnsafeVariance V>) {
            table.forEach { (r: R, c: C, v: V) -> this.put(r, c, v) }
        }
    }
}

interface MutableTable<R, C, out V> : Table<R, C, V> {
    /**
     * 指定した値を追加します。
     */
    fun put(row: R, column: C, value: @UnsafeVariance V): V?

    /**
     * 指定した値を追加します。
     */
    fun put(triple: Triple<R, C, @UnsafeVariance V>): V? = put(triple.first, triple.second, triple.third)

    /**
     * 指定した値を追加します。
     */
    operator fun set(row: R, column: C, value: @UnsafeVariance V) {
        put(row, column, value)
    }

    /**
     * 指定した値を追加します。
     */
    fun putAll(triples: Iterable<Triple<R, C, @UnsafeVariance V>>) {
        triples.forEach(::put)
    }

    /**
     * 指定した値を追加します。
     */
    fun putAll(triples: Sequence<Triple<R, C, @UnsafeVariance V>>) {
        triples.forEach(::put)
    }

    /**
     * 指定した値を追加します。
     */
    fun putAll(triples: Array<out Triple<R, C, @UnsafeVariance V>>) {
        triples.forEach(::put)
    }

    /**
     * ほかのテーブルから値を追加します。
     */
    fun putAll(table: Table<out R, out C, @UnsafeVariance V>) {
        table.forEach { (r: R, c: C, v: V) -> this.put(r, c, v) }
    }

    /**
     * 指定した[row]と[column]に紐づいた値を削除します。
     * @return 以前に結びついていた値
     */
    fun remove(row: R, column: C): V?

    /**
     * すべての値を消去します。
     */
    fun clear()
}
