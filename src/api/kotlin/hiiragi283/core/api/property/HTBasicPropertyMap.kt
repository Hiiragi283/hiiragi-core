package hiiragi283.core.api.property

/**
 * [Map]に基づいた[HTPropertyKey]の抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
sealed class HTBasicPropertyMap(protected open val map: Map<HTPropertyKey<*>, Any>) : HTPropertyMap {
    override fun isEmpty(): Boolean = map.isEmpty()

    override fun contains(key: HTPropertyKey<*>): Boolean = key in map

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: HTPropertyKey<T>): T? = map[key] as? T

    /**
     * [Map]に基づいた[HTPropertyMap.Mutable]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.6.0
     */
    class Immutable(map: Map<HTPropertyKey<*>, Any>) : HTBasicPropertyMap(map) {
        constructor() : this(emptyMap())
    }

    /**
     * [MutableMap]に基づいた[HTPropertyMap.Mutable]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.6.0
     */
    class Mutable(override val map: MutableMap<HTPropertyKey<*>, Any>) :
        HTBasicPropertyMap(map),
        HTPropertyMap.Mutable {
        constructor() : this(hashMapOf())

        @Suppress("UNCHECKED_CAST")
        override fun <T> put(key: HTPropertyKey<T>, value: T): T? {
            if (value == null) return remove(key)
            return map.put(key, value) as? T
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T> remove(key: HTPropertyKey<T>): T? = map.remove(key) as? T
    }
}
