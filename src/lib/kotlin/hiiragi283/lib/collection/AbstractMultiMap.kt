package hiiragi283.lib.collection

abstract class AbstractMultiMap<K, out V, out C : Collection<V>>(protected val map: Map<K, C>) : MultiMap<K, V> {
    override val size: Int get() = map.size

    override fun isEmpty(): Boolean = map.isEmpty()

    override fun containsKey(key: K): Boolean = key in map

    override fun containsValue(value: @UnsafeVariance V): Boolean = map.any { (_, values: C) -> value in values }

    override fun get(key: K): C = map[key] ?: emptyCollection()

    protected abstract fun emptyCollection(): C

    override val keys: Set<K> get() = map.keys
    override val values: Collection<V> get() = map.values.flatten()
    override val entries: Set<Pair<K, V>> get() = map.entries.flatMapTo(mutableSetOf()) { (key: K, values: C) -> values.map { key to it } }

    override fun asMap(): Map<K, C> = map

    abstract class Builder<K, out V, out C : MutableCollection<@UnsafeVariance V>>(protected val map: MutableMap<K, @UnsafeVariance C>) : MultiMap.Builder<K, V> {
        protected fun get(key: K): C = map.getOrPut(key, ::emptyCollection)

        protected abstract fun emptyCollection(): C

        override fun put(key: K, value: @UnsafeVariance V): Boolean = this.get(key).add(value)

        override fun putAll(key: K, values: Iterable<@UnsafeVariance V>): Boolean = this.get(key).addAll(values)
    }
}
