package hiiragi283.core.api.collection

/**
 * @author Hiiragi Tsubasa
 * @since 0.2.0
 */
class HTSetMultiMap<K : Any, V : Any>(internalMap: Map<K, Set<V>>) : HTAbstractMultiMap<K, V, Set<V>>(internalMap) {
    /**
     * @author Hiiragi Tsubasa
     * @since 0.2.0
     */
    class Mutable<K : Any, V : Any>(internalMap: MutableMap<K, MutableSet<V>>) :
        HTAbstractMultiMap.Mutable<K, V, MutableSet<V>>(internalMap) {
        override fun initCollection(key: K): MutableSet<V> = mutableSetOf()

        override fun wrapIterable(values: Iterable<V>): MutableSet<V> = values.toMutableSet()
    }
}
