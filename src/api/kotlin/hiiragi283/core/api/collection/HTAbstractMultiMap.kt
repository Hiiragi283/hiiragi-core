package hiiragi283.core.api.collection

/**
 * @author Hiiragi Tsubasa
 * @since 0.2.0
 * @see AbstractMap
 */
abstract class HTAbstractMultiMap<K : Any, V : Any, C : Collection<V>>(protected open val internalMap: Map<K, C>) : HTMultiMap<K, V> {
    override val size: Int
        get() = internalMap.size
    override val isEmpty: Boolean
        get() = internalMap.isEmpty() || internalMap.values.all { it.isEmpty() }

    override fun containsKey(key: K): Boolean = internalMap.containsKey(key)

    override fun containsValue(value: V): Boolean = entries.any { it.second == value }

    override fun get(key: K): Collection<V> = internalMap[key] ?: emptyList()

    override val keys: Set<K>
        get() = internalMap.keys
    override val values: Collection<V>
        get() = internalMap.values.flatten()
    override val entries: Set<Pair<K, V>>
        get() = internalMap.entries.flatMap { (key: K, values: C) -> values.map { key to it } }.toSet()
    override val map: Map<K, Collection<V>>
        get() = internalMap

    /**
     * @author Hiiragi Tsubasa
     * @since 0.2.0
     * @see AbstractMutableMap
     */
    abstract class Mutable<K : Any, V : Any, C : MutableCollection<V>>(override val internalMap: MutableMap<K, C>) :
        HTAbstractMultiMap<K, V, C>(internalMap),
        HTMultiMap.Mutable<K, V> {
        protected fun getCollection(key: K): C = internalMap.computeIfAbsent(key, ::initCollection)

        protected abstract fun initCollection(key: K): C

        override fun put(key: K, value: V): Boolean = getCollection(key).add(value)

        override fun replaceValues(key: K, values: Iterable<V>): Collection<V> = internalMap.put(key, wrapIterable(values)) ?: emptySet()

        protected abstract fun wrapIterable(values: Iterable<V>): C

        override fun remove(key: K, value: V): Boolean = getCollection(key).remove(value)

        override fun removeAll(key: K): Collection<V> = internalMap.remove(key) ?: emptySet()

        override fun clear() {
            internalMap.clear()
        }
    }
}
