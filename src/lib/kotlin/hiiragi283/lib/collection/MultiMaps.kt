package hiiragi283.lib.collection

@Suppress("UNCHECKED_CAST")
fun <K, V> emptyMultiMapOf(): MultiMap<K, V> = EmptyMultiMap as MultiMap<K, V>

private data object EmptyMultiMap : MultiMap<Nothing, Nothing> {
    override val size: Int = 0

    override fun isEmpty(): Boolean = true

    override fun containsKey(key: Nothing): Boolean = false

    override fun containsValue(value: Nothing): Boolean = false

    override fun get(key: Nothing): Collection<Nothing> = setOf()

    override val keys: Set<Nothing> = setOf()
    override val values: Collection<Nothing> = setOf()
    override val entries: Set<Pair<Nothing, Nothing>> = setOf()

    override fun asMap(): Map<Nothing, Collection<Nothing>> = mapOf()
}
