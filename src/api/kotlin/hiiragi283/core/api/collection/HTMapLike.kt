package hiiragi283.core.api.collection

interface HTMapLike<K, V> : Iterable<Map.Entry<K, V>> {
    val isEmpty: Boolean

    operator fun get(key: K): V?

    val keys: Set<K>
}
