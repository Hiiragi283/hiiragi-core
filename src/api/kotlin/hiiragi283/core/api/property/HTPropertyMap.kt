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
     * このマップが空でないか判定します。
     */
    fun isNotEmpty(): Boolean = !isEmpty()

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
