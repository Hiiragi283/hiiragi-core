package hiiragi283.core.api.serialization.component

import net.minecraft.core.component.DataComponentType

interface HTComponentInput {
    operator fun <T : Any> get(type: DataComponentType<T>): T?

    fun <T : Any, R> use(type: DataComponentType<T>, action: (T) -> R): R? = get(type)?.let(action)
}
