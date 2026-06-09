package hiiragi283.lib.collection

class SetMultiMap<K, out V> private constructor(map: Map<K, Set<V>>) : AbstractMultiMap<K, V, Set<V>>(map) {
    companion object {
        @JvmStatic
        fun <K, V> copyOf(map: Map<K, Set<V>>): MultiMap<K, V> = when {
            map.isDeepEmpty() -> emptyMultiMapOf()
            else -> SetMultiMap(map)
        }
    }

    override fun emptyCollection(): Set<V> = setOf()

    class Builder<K, out V> : AbstractMultiMap.Builder<K, V, MutableSet<@UnsafeVariance V>> {
        constructor(map: MutableMap<K, MutableSet<V>>) : super(map)

        constructor(initialCapacity: Int = 10) : super(initialCapacity)

        constructor(other: MultiMap<K, V>) : super(other)

        override fun emptyCollection(): MutableSet<@UnsafeVariance V> = mutableSetOf()

        override fun build(): MultiMap<K, V> = when {
            map.isDeepEmpty() -> emptyMultiMapOf()
            else -> SetMultiMap(map)
        }
    }
}
