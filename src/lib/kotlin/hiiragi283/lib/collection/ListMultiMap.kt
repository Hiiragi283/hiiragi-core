package hiiragi283.lib.collection

class ListMultiMap<K, out V>(map: Map<K, List<V>>) : AbstractMultiMap<K, V, List<V>>(map) {
    override fun emptyCollection(): List<V> = listOf()
}

class MutableListMultiMap<K, out V>(map: MutableMap<K, MutableList<V>>) : AbstractMutableMultiMap<K, V, MutableList<@UnsafeVariance V>>(map) {
    constructor() : this(mutableMapOf())

    override fun emptyCollection(): MutableList<@UnsafeVariance V> = mutableListOf()
}
