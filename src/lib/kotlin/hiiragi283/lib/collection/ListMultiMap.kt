package hiiragi283.lib.collection

class ListMultiMap<K, out V> private constructor(map: Map<K, List<V>>) : AbstractMultiMap<K, V, List<V>>(map) {
    companion object {
        @JvmStatic
        fun <K, V> copyOf(map: Map<K, List<V>>): MultiMap<K, V> = when {
            map.isDeepEmpty() -> emptyMultiMapOf()
            else -> ListMultiMap(map)
        }
    }

    override fun emptyCollection(): List<V> = listOf()

    class Builder<K, out V> : AbstractMultiMap.Builder<K, V, MutableList<@UnsafeVariance V>> {
        constructor(map: MutableMap<K, MutableList<V>>) : super(map)

        constructor(initialCapacity: Int = 10) : super(initialCapacity)

        constructor(other: MultiMap<K, V>) : super(other)

        override fun emptyCollection(): MutableList<@UnsafeVariance V> = mutableListOf()

        override fun build(): MultiMap<K, V> = when {
            map.isDeepEmpty() -> emptyMultiMapOf()
            else -> ListMultiMap(map)
        }
    }
}
