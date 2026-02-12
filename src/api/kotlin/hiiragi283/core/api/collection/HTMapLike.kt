package hiiragi283.core.api.collection

/**
 * [Map]の一部を切り出したインターフェースです。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
interface HTMapLike<K, V> : Iterable<Map.Entry<K, V>> {
    /**
     * このマップが空か判定します。
     */
    val isEmpty: Boolean

    /**
     * 指定した[key]に対応する値を取得します。
     */
    operator fun get(key: K): V?

    /**
     * このマップに含まれるすべてのキーの一覧を返します。
     */
    val keys: Set<K>
}
