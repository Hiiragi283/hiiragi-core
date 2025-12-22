package hiiragi283.core.api.collection

import com.google.common.collect.Multimap

/**
 * 一つのキーに対して複数の値で構成されるマップを表すインターフェースです。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see Multimap
 */
interface HTMultiMap<K : Any, V : Any> {
    /**
     * このマップに含まれるペアの個数を返します。
     */
    val size: Int

    /**
     * このマップが空か判定します。
     */
    val isEmpty: Boolean

    /**
     * 指定した[key]がこのマップに含まれているか判定します。
     */
    fun containsKey(key: K): Boolean

    /**
     * 指定した[value]がこのマップに含まれているか判定します。
     */
    fun containsValue(value: V): Boolean

    /**
     * 指定した[key]から対応する値の一覧を返します。
     */
    operator fun get(key: K): Collection<V>

    /**
     * このマップに含まれるすべてのキーの一覧を返します。
     */
    val keys: Set<K>

    /**
     * このマップに含まれるすべての値の一覧を返します。
     */
    val values: Collection<V>

    /**
     * このマップに含まれるすべてのペアの一覧を返します。
     */
    val entries: Set<Pair<K, V>>

    /**
     * このマップを[Map]に変換します。
     */
    val map: Map<K, Collection<V>>

    /**
     * 可変な[HTMultiMap]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    interface Mutable<K : Any, V : Any> : HTMultiMap<K, V> {
        /**
         * 指定した[key]と[value]を追加します。
         * @return マップが更新された場合は`true`
         */
        fun put(key: K, value: V): Boolean

        /**
         * 指定した[key]と[value]を追加します。
         */
        operator fun set(key: K, value: V) {
            put(key, value)
        }

        /**
         * 指定した[key]と[values]を追加します。
         */
        fun putAll(key: K, values: Iterable<V>) {
            for (value: V in values) {
                put(key, value)
            }
        }

        /**
         * 指定した[key]に対応する値を[values]で置換します。
         * @return 置換される前の値の一覧
         */
        fun replaceValues(key: K, values: Iterable<V>): Collection<V>

        /**
         * 指定した[key]に対応する[value]を削除します。
         * @return マップが更新された場合は`true`
         */
        fun remove(key: K, value: V): Boolean

        /**
         * 指定した[key]に対応するすべての値を削除します。
         * @return キーに対応していた値の一覧
         */
        fun removeAll(key: K): Collection<V>

        /**
         * すべてのキーと値を削除します。
         */
        fun clear()
    }
}
