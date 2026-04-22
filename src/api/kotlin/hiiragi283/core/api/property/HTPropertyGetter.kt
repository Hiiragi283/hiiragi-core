package hiiragi283.core.api.property

/**
 * [HTPropertyKey]に基づいてデータを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.3
 */
interface HTPropertyGetter {
    /**
     * 指定した[key]が含まれるか判定します。
     */
    operator fun contains(key: HTPropertyKey<*>): Boolean = get(key) != null

    /**
     * 指定した[key]に紐づいた値を返します。
     * @return 値がない場合は`null`
     */
    operator fun <T> get(key: HTPropertyKey<T>): T?
}

//    Extensions    //

/**
 * 指定した[key]に紐づいた値を返します。
 * @throws IllegalStateException 値がない場合
 */
fun <T : Any> HTPropertyGetter.getOrThrow(key: HTPropertyKey<T?>): T = get(key) ?: error("Unbounded property: ${key.id}")

/**
 * 指定した[key]に紐づいた値を返します。
 * @return 値がない場合は[デフォルト値][HTPropertyKey.defaultValue]
 */
fun <T : Any> HTPropertyGetter.getOrDefault(key: HTPropertyKey<T>): T = get(key) ?: key.defaultValue
