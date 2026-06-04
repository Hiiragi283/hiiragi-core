package hiiragi283.lib.collection

/**
 * @see com.google.common.collect.Multimap
 */
interface MultiMap<K, out V> {
    val size: Int

    fun isEmpty(): Boolean

    fun containsKey(key: K): Boolean

    operator fun contains(key: K): Boolean = containsKey(key)

    fun containsValue(value: @UnsafeVariance V): Boolean

    operator fun get(key: K): Collection<V>

    val keys: Set<K>
    val values: Collection<V>
    val entries: Set<Pair<K, V>>

    fun asMap(): Map<K, Collection<V>>
}

interface MutableMultiMap<K, out V> : MultiMap<K, V> {
    override fun get(key: K): MutableCollection<@UnsafeVariance V>

    override fun asMap(): Map<K, MutableCollection<@UnsafeVariance V>>

    fun put(key: K, value: @UnsafeVariance V): Boolean

    operator fun set(key: K, value: @UnsafeVariance V) {
        put(key, value)
    }

    fun putAll(key: K, values: Iterable<@UnsafeVariance V>): Boolean

    fun removeAll(key: K): MutableCollection<@UnsafeVariance V>

    fun clear()
}
