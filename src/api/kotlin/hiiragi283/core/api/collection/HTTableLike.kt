package hiiragi283.core.api.collection

/**
 * [HTTable]の一部を切り出したインターフェースです。
 * @param R 行のクラス
 * @param C 列のクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
interface HTTableLike<R, C, V> : Iterable<Triple<R, C, V>> {
    /**
     * このテーブルが空か判定します。
     */
    val isEmpty: Boolean

    /**
     * 指定した[row]に対応する列と値のマップを返します。
     */
    operator fun get(row: R, column: C): V?

    /**
     * 指定した[column]に対応する行と値のマップを返します。
     */
    fun row(row: R): HTMapLike<C, V>

    fun column(column: C): HTMapLike<R, V>

    /**
     * このテーブルに含まれるすべての行のキーの一覧を返します。
     */
    val rowKeys: Set<R>

    /**
     * このテーブルに含まれるすべての値の一覧を返します。
     */
    val columnKeys: Set<C>
}
