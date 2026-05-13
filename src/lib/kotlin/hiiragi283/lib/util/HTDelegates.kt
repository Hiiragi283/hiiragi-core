package hiiragi283.lib.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
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
}
