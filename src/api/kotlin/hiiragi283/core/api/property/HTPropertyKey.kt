package hiiragi283.core.api.property

import net.minecraft.resources.ResourceLocation

class HTPropertyKey<T> private constructor(val id: ResourceLocation, val defaultValue: T) {
    companion object {
        @JvmStatic
        private val instance: MutableMap<ResourceLocation, HTPropertyKey<*>> = hashMapOf()

        @JvmStatic
        fun <T : Any> createNullable(id: ResourceLocation): HTPropertyKey<T?> = create(id, null)

        @JvmStatic
        fun <T> create(id: ResourceLocation, defaultValue: T): HTPropertyKey<T> {
            val key: HTPropertyKey<T> = HTPropertyKey(id, defaultValue)
            check(instance.put(id, key) == null) { "Duplicated material attribute key: $id" }
            return key
        }
    }
}
