package hiiragi283.core.api.serialization.component

import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentType

interface DataComponentGetter {
    operator fun <T : Any> get(type: DataComponentType<T>): T?

    fun <T : Any> getOrDefault(type: DataComponentType<T>, defaultValue: T): T = get(type) ?: defaultValue

    fun <T : Any, R> use(type: DataComponentType<T>, action: (T) -> R): R? = get(type)?.let(action)
}

fun DataComponentGetter(map: DataComponentMap): DataComponentGetter = MapDataComponentGetter(map)

@JvmRecord
private data class MapDataComponentGetter(val map: DataComponentMap) : DataComponentGetter {
    override fun <T : Any> get(type: DataComponentType<T>): T? = map.get(type)
}

fun DataComponentGetter(holder: DataComponentHolder): DataComponentGetter = HolderDataComponentGetter(holder)

@JvmRecord
private data class HolderDataComponentGetter(val holder: DataComponentHolder) : DataComponentGetter {
    override fun <T : Any> get(type: DataComponentType<T>): T? = holder.get(type)
}
