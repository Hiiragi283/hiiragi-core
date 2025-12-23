package hiiragi283.core.api.collection

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

/**
 * 一つのキーに対して複数の値で構成されるマップを表すクラスです。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.2.1
 * @see Multimap
 */
@JvmInline
value class ImmutableMultiMap<K : Any, V : Any>(private val multimap: Multimap<K, V>) {
    /**
     * このマップに含まれるペアの個数を返します。
     */
    val size: Int get() = multimap.size()

    /**
     * このマップが空か判定します。
     */
    val isEmpty: Boolean get() = multimap.isEmpty

    /**
     * 指定した[key]がこのマップに含まれているか判定します。
     */
    fun containsKey(key: K): Boolean = multimap.containsKey(key)

    operator fun contains(key: K): Boolean = containsKey(key)

    /**
     * 指定した[value]がこのマップに含まれているか判定します。
     */
    fun containsValue(value: V): Boolean = multimap.containsValue(value)

    /**
     * 指定した[key]から対応する値の一覧を返します。
     */
    operator fun get(key: K): Collection<V> = multimap.get(key)

    /**
     * このマップに含まれるすべてのキーの一覧を返します。
     */
    val keys: Set<K> get() = multimap.keySet()

    /**
     * このマップに含まれるすべての値の一覧を返します。
     */
    val values: Collection<V> get() = multimap.values()

    /**
     * このマップに含まれるすべてのペアの一覧を返します。
     */
    val entries: Collection<Map.Entry<K, V>> get() = multimap.entries()

    /**
     * このマップを[Map]に変換します。
     */
    val map: Map<K, Collection<V>> get() = multimap.asMap()

    //    Extensions    //

    inline fun forEach(action: (Map.Entry<K, V>) -> Unit) {
        entries.forEach(action)
    }

    //    Builder    //

    /**
     * [ImmutableMultiMap]のビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.2.1
     */
    class Builder<K : Any, V : Any>(initialCapacity: Int = 10, perKey: Int = 2) {
        private val values: Multimap<K, V> = HashMultimap.create<K, V>(initialCapacity, perKey)

        /**
         * 指定した[key]と[value]を追加します。
         */
        fun put(key: K, value: V): Builder<K, V> {
            values.put(key, value)
            return this
        }

        /**
         * 指定した[key]と[value]を追加します。
         */
        operator fun set(key: K, value: V) {
            put(key, value)
        }

        /**
         * 指定した[key]と[values]を追加します。
         */
        fun putAll(key: K, values: Iterable<V>): Builder<K, V> {
            this.values.putAll(key, values)
            return this
        }

        /**
         * 指定した[key]と[values]を追加します。
         */
        fun putAll(key: K, vararg values: V): Builder<K, V> {
            for (value: V in values) {
                put(key, value)
            }
            return this
        }

        fun putAll(map: Map<K, V>): Builder<K, V> {
            map.forEach { (key: K, value: V) -> this.values.put(key, value) }
            return this
        }

        fun putAll(multiMap: ImmutableMultiMap<K, V>): Builder<K, V> {
            multiMap.forEach { (key: K, value: V) -> this.values.put(key, value) }
            return this
        }

        /**
         * 指定した[key]に対応する値を[values]で置換します。
         */
        fun replaceValues(key: K, values: Iterable<V>): Builder<K, V> {
            this.values.putAll(key, values)
            return this
        }

        /**
         * 新しい[ImmutableMultiMap]のインスタンスを返します。
         */
        fun build(): ImmutableMultiMap<K, V> = when {
            values.isEmpty -> immutableMultiMapOf()
            else -> ImmutableMultiMap(values)
        }
    }
}
