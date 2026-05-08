package hiiragi283.core.api.property

/**
 * [HTPropertyGetter]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
interface HTPropertyMap : HTPropertyGetter {
    val size: Int

    val isEmpty: Boolean get() = size == 0

    /**
     * 何も値を返さない[HTPropertyMap]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.16.0
     */
    data object Empty : HTPropertyMap {
        override val size: Int = 0

        override val isEmpty: Boolean = true

        override fun <T> get(key: HTPropertyKey<T>): T? = null
    }

    /**
     * [HTPropertyMap]のビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.16.0
     */
    class Builder private constructor(private val map: MutableMap<HTPropertyKey<*>, Any>, delegate: HTPropertyMap) : HTPropertyMap by delegate {
        constructor() : this(hashMapOf())

        constructor(map: MutableMap<HTPropertyKey<*>, Any>) : this(map, SimpleMap(map))

        /**
         * 指定した[key]と[value]を追加します。
         * @return 以前に紐づいていた値
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> put(key: HTPropertyKey<T>, value: T): T? {
            if (value == null) return remove(key)
            return map.put(key, value) as? T
        }

        /**
         * 指定した[key]と[value]を追加します。
         */
        operator fun <T> set(key: HTPropertyKey<T>, value: T) {
            put(key, value)
        }

        /**
         * 指定した[key]を削除します。
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> remove(key: HTPropertyKey<T>): T? = map.remove(key) as? T

        fun build(): HTPropertyMap = when {
            map.isEmpty() -> Empty
            else -> SimpleMap(map)
        }
    }

    /**
     * [Map]に基づいた[HTPropertyMap]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.6.0
     */
    private data class SimpleMap(private val map: Map<HTPropertyKey<*>, Any>) : HTPropertyMap {
        override val size: Int get() = map.size

        override val isEmpty: Boolean get() = map.isEmpty()

        @Suppress("UNCHECKED_CAST")
        override fun <T> get(key: HTPropertyKey<T>): T? = map[key] as? T

        override fun contains(key: HTPropertyKey<*>): Boolean = key in map
    }
}

//    Extensions    //

inline fun buildPropertyMap(builderAction: HTPropertyMap.Builder.() -> Unit): HTPropertyMap = HTPropertyMap.Builder().apply(builderAction).build()

/**
 * @since 0.9.0
 */
fun HTPropertyMap.Builder.add(key: HTPropertyKey<Unit?>) {
    this.put(key, Unit)
}

/**
 * @since 0.9.0
 */
operator fun HTPropertyMap.Builder.plusAssign(key: HTPropertyKey<Unit?>) {
    this.add(key)
}

/**
 * @see MutableMap.computeIfAbsent
 */
inline fun <T : Any> HTPropertyMap.Builder.computeIfAbsent(key: HTPropertyKey<T?>, mapping: () -> T): T {
    val oldValue: T? = get(key) ?: key.defaultValue
    if (oldValue == null) {
        val newValue: T = mapping()
        put(key, newValue)
        return newValue
    } else {
        return oldValue
    }
}

/**
 * @see MutableMap.computeIfAbsent
 */
inline fun <T : Any> HTPropertyMap.Builder.computeIfAbsent(key: HTPropertyKey<T>, mapping: (T) -> T): T {
    val newValue: T = (get(key) ?: key.defaultValue).let(mapping)
    put(key, newValue)
    return newValue
}
