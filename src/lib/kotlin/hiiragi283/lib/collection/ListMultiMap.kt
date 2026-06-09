package hiiragi283.lib.collection

class ListMultiMap<K, out V> private constructor(map: Map<K, List<V>>) : AbstractMultiMap<K, V, List<V>>(map) {
    override fun emptyCollection(): List<V> = listOf()

    class Builder<K, out V>(map: MutableMap<K, MutableList<V>>) : AbstractMultiMap.Builder<K, V, MutableList<@UnsafeVariance V>>(map) {
        override fun emptyCollection(): MutableList<@UnsafeVariance V> = mutableListOf()

        override fun build(): MultiMap<K, V> = when {
            map.isEmpty() -> emptyMultiMapOf()
            else -> ListMultiMap(map)
        }
    }
}
