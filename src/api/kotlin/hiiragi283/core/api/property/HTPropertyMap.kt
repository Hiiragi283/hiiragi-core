package hiiragi283.core.api.property

/**
 * [HTPropertyKey]に基づいてデータを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
interface HTPropertyMap {
    /**
     * このマップが空か判定します。
     */
    fun isEmpty(): Boolean

    /**
     * 指定した[key]が含まれるか判定します。
     */
    operator fun contains(key: HTPropertyKey<*>): Boolean

    /**
     * 指定した[key]に紐づいた値を返します。
     * @return 値がない場合は`null`
     */
    operator fun <T> get(key: HTPropertyKey<T>): T?

    /**
     * 可変な[HTPropertyMap]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.6.0
     */
    interface Mutable : HTPropertyMap {
        /**
         * 指定した[key]と[value]を追加します。
         * @return 以前に紐づいていた値
         */
        fun <T> put(key: HTPropertyKey<T>, value: T): T?

        /**
         * 指定した[key]と[value]を追加します。
         */
        operator fun <T> set(key: HTPropertyKey<T>, value: T) {
            put(key, value)
        }

        /**
         * 指定した[key]を削除します。
         */
        fun <T> remove(key: HTPropertyKey<T>): T?
    }
}

/**
 * このマップが空でないか判定します。
 */
fun HTPropertyMap.isNotEmpty(): Boolean = !this.isEmpty()

/**
 * 指定した[key]に紐づいた値を返します。
 * @throws IllegalStateException 値がない場合
 */
fun <T : Any> HTPropertyMap.getOrThrow(key: HTPropertyKey<T?>): T = get(key) ?: error("Unbounded property: ${key.id}")

/**
 * 指定した[key]に紐づいた値を返します。
 * @return 値がない場合は[デフォルト値][HTPropertyKey.defaultValue]
 */
fun <T : Any> HTPropertyMap.getOrDefault(key: HTPropertyKey<T>): T = get(key) ?: key.defaultValue

//    Mutable    //

/**
 * @since 0.9.0
 */
fun HTPropertyMap.Mutable.add(key: HTPropertyKey<Unit?>) {
    this.put(key, Unit)
}

/**
 * @since 0.9.0
 */
operator fun HTPropertyMap.Mutable.plusAssign(key: HTPropertyKey<Unit?>) {
    this.add(key)
}

/**
 * @see MutableMap.computeIfAbsent
 */
inline fun <T : Any> HTPropertyMap.Mutable.computeIfAbsent(key: HTPropertyKey<T?>, mapping: () -> T): T {
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
inline fun <T : Any> HTPropertyMap.Mutable.computeIfAbsent(key: HTPropertyKey<T>, mapping: (T) -> T): T {
    val newValue: T = (get(key) ?: key.defaultValue).let(mapping)
    put(key, newValue)
    return newValue
}
