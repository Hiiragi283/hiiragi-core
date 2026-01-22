package hiiragi283.core.api.property

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
