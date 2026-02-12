package hiiragi283.core.api.collection

/**
 * 二つの[Map]に基づいた[HTMapLike]の実装クラスです。
 * @param prototype 基本となる[マップ][Map]
 * @param patch パッチとなる[マップ][Map]
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 * @see net.minecraft.core.component.PatchedDataComponentMap
 */
class HTPatchedMap<K, V>(private val prototype: Map<K, V>, private val patch: Map<K, V>) : HTMapLike<K, V> {
    override val isEmpty: Boolean
        get() = prototype.isEmpty() && patch.isEmpty()

    override operator fun get(key: K): V? = patch[key] ?: prototype[key]

    override val keys: Set<K> get() = prototype.keys.plus(patch.keys)

    override fun iterator(): Iterator<Map.Entry<K, V>> = iterator {
        yieldAll(prototype.entries)
        yieldAll(patch.entries)
    }
}
