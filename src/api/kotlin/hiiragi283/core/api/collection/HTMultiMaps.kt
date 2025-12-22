package hiiragi283.core.api.collection

fun <K : Any, V : Any> Iterable<Pair<K, Iterable<V>>>.toMultiMap(): HTMultiMap<K, V> {
    val multimap: HTMultiMap.Mutable<K, V> = HTSetMultiMap.Mutable(mutableMapOf())
    for ((key: K, values: Iterable<V>) in this) {
        multimap.putAll(key, values)
    }
    return multimap
}

inline fun <K : Any, V : Any> Iterable<K>.associateMulti(transform: (K) -> Pair<K, Iterable<V>>): HTMultiMap<K, V> =
    this.map(transform).toMultiMap()

inline fun <K : Any, V : Any> Iterable<K>.associateMultiWith(transform: (K) -> Iterable<V>): HTMultiMap<K, V> =
    this.map { key: K -> key to transform(key) }.toMultiMap()

inline fun <K : Any, V : Any> buildMultiMap(builderAction: HTMultiMap.Mutable<K, V>.() -> Unit): HTMultiMap<K, V> =
    HTSetMultiMap.Mutable<K, V>(mutableMapOf()).apply(builderAction)

fun <K : Any, V : Any> HTMultiMap<K, V>.asSequence(): Sequence<Pair<K, V>> = this.entries.asSequence()
