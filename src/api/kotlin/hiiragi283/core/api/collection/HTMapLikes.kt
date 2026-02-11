package hiiragi283.core.api.collection

fun <K, V> Map<K, V>.toLike(): HTMapLike<K, V> = object : HTMapLike<K, V> {
    override val isEmpty: Boolean
        get() = this@toLike.isEmpty()

    override fun get(key: K): V? = this@toLike[key]

    override val keys: Set<K>
        get() = this@toLike.keys

    override fun iterator(): Iterator<Map.Entry<K, V>> = this@toLike.entries.iterator()
}
