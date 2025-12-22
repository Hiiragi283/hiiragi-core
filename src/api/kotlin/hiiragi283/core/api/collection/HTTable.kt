package hiiragi283.core.api.collection

import com.google.common.collect.Table

/**
 * 二つのキーに対して一つの値で構成されるコレクションを表すインターフェースです。
 * @param R 行のクラス
 * @param C 列のクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see Table
 */
interface HTTable<R : Any, C : Any, V : Any> {
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
    fun containsValue(value: V): Boolean

    /**
     * 指定した[row]と[column]から対応する値を返します。
     */
    operator fun get(row: R, column: C): V?

    /**
     * このテーブルが空か判定します。
     */
    fun isEmpty(): Boolean

    /**
     * このマップに含まれる組の個数を返します。
     */
    val size: Int

    /**
     * 指定した[row]に対応する列と値のマップを返します。
     */
    fun row(row: R): Map<C, V>

    /**
     * 指定した[column]に対応する行と値のマップを返します。
     */
    fun column(column: C): Map<R, V>

    /**
     * このマップに含まれるすべての行のキーの一覧を返します。
     */
    val rowKeys: Set<R>

    /**
     * このマップに含まれるすべての列のキーの一覧を返します。
     */
    val columnKeys: Set<C>

    /**
     * このマップに含まれるすべての値の一覧を返します。
     */
    val values: Collection<V>

    /**
     * このマップに含まれるすべての組の一覧を返します。
     */
    val entries: Set<Triple<R, C, V>>

    val rowMap: Map<R, Map<C, V>>

    val columnMap: Map<C, Map<R, V>>

    fun forEach(action: (Triple<R, C, V>) -> Unit) {
        entries.forEach(action)
    }
}
