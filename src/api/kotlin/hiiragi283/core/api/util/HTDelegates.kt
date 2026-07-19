package hiiragi283.core.api.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 * @see kotlin.properties.Delegates
 */
data object HTDelegates {
    /**
     * 一度だけ値を代入可能なプロパティを返します。
     */
    fun <T : Any> onceInitialize(): ReadWriteProperty<Any?, T> = OnceInitialize()

    private class OnceInitialize<T : Any> : ReadWriteProperty<Any?, T> {
        private var value: T? = null

        override fun getValue(thisRef: Any?, property: KProperty<*>): T = value ?: error("Property ${property.name} has not initialized")

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            check(this.value == null) { "Property ${property.name} has already initialized" }
            this.value = value
        }
    }

    /**
     * 一度だけ値を代入可能なプロパティを返します。
     */
    fun <T : Any> optionalOnceInitialize(): ReadWriteProperty<Any?, Option<T>> = OptionalOnceInitialize()

    private class OptionalOnceInitialize<T : Any> : ReadWriteProperty<Any?, Option<T>> {
        private var value: Option<T> = Option.none()
        private var initialized: Boolean = false

        override fun getValue(thisRef: Any?, property: KProperty<*>): Option<T> = value

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Option<T>) {
            check(!initialized) { "Property ${property.name} has already initialized" }
            this.value = value
        }
    }
}
