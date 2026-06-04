package hiiragi283.lib.collection

class SetMultiMap<K, out V>(map: Map<K, Set<V>>) : AbstractMultiMap<K, V, Set<V>>(map) {
    override fun emptyCollection(): Set<V> = setOf()
}

class MutableSetMultiMap<K, out V>(map: MutableMap<K, MutableSet<V>>) : AbstractMutableMultiMap<K, V, MutableSet<@UnsafeVariance V>>(map) {
    constructor() : this(mutableMapOf())

    override fun emptyCollection(): MutableSet<@UnsafeVariance V> = mutableSetOf()
}
