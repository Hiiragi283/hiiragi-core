package hiiragi283.core.api.property

/**
 * [Map]に基づいた[HTPropertyGetter]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
class HTPropertyMap private constructor(private val map: Map<HTPropertyKey<*>, Any>) : HTPropertyGetter {
    companion object {
        /**
         * 指定した[map]から[HTPropertyMap]のインスタンスを作成します。
         * @return [map]が空の場合は[Empty]
         */
        @JvmStatic
        fun create(map: Map<HTPropertyKey<*>, Any>): HTPropertyGetter = when {
            map.isEmpty() -> Empty
            else -> HTPropertyMap(map)
        }
    }

    override fun contains(key: HTPropertyKey<*>): Boolean = key in map

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: HTPropertyKey<T>): T? = map[key] as? T

    /**
     * 何も値を返さない[HTPropertyGetter]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 0.15.3
     */
    data object Empty : HTPropertyGetter {
        override fun <T> get(key: HTPropertyKey<T>): T? = null
    }

    /**
     * [HTPropertyMap]のビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.15.3
     */
    class Builder private constructor(private val map: MutableMap<HTPropertyKey<*>, Any>, delegate: HTPropertyMap) :
        HTPropertyGetter by delegate {
            constructor() : this(hashMapOf())

            constructor(map: MutableMap<HTPropertyKey<*>, Any>) : this(map, HTPropertyMap(map))

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

            fun build(): HTPropertyGetter = create(map)
        }
}

//    Extensions    //

inline fun buildPropertyMap(builderAction: HTPropertyMap.Builder.() -> Unit): HTPropertyGetter =
    HTPropertyMap.Builder().apply(builderAction).build()

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
