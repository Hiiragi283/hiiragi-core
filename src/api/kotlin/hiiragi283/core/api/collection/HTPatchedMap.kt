package hiiragi283.core.api.collection

/**
 * @see net.minecraft.core.component.PatchedDataComponentMap
 */
class HTPatchedMap<K, V>(private val prototype: Map<K, V>, private val patch: Map<K, V>) : HTMapLike<K, V> {
    override val isEmpty: Boolean
        get() = prototype.isEmpty() && patch.isEmpty()

    override operator fun get(key: K): V? = prototype[key] ?: patch[key]

    override val keys: Set<K> get() = prototype.keys.plus(patch.keys)

    override fun iterator(): Iterator<Map.Entry<K, V>> = iterator {
        yieldAll(prototype.entries)
        yieldAll(patch.entries)
    }
}
