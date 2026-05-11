package hiiragi283.lib.util

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class HTSimpleProperty<T>(initialValue: T) : ReadWriteProperty<Any?, T> {
    private var value: T = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

//    List    //

fun <T> List<T>.createProperty(index: Int): ReadOnlyProperty<Any?, T> = ReadOnlyProperty { _, _ -> this[index] }

fun <T> MutableList<T>.createProperty(index: Int): ReadWriteProperty<Any?, T> = object : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = this@createProperty[index]

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this@createProperty[index] = value
    }
}
