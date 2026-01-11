package hiiragi283.core.api.property

interface HTPropertyMap {
    fun isEmpty(): Boolean

    operator fun contains(key: HTPropertyKey<*>): Boolean

    operator fun <T> get(key: HTPropertyKey<T>): T?

    fun <T> getOrDefault(key: HTPropertyKey<T>): T = get(key) ?: key.defaultValue

    fun <T> getOrThrow(key: HTPropertyKey<T>): T & Any = get(key) ?: error("Unbounded property: ${key.id}")

    interface Mutable : HTPropertyMap {
        fun <T> put(key: HTPropertyKey<T>, value: T): T?

        operator fun <T> set(key: HTPropertyKey<T>, value: T) {
            put(key, value)
        }

        fun <T> remove(key: HTPropertyKey<T>): T?
    }
}
