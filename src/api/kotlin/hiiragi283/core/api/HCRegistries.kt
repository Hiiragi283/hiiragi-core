package hiiragi283.core.api

import hiiragi283.core.api.HiiragiCoreAPI.id
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

/**
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
object HCRegistries {
    @JvmField
    val SLOT_TYPE: Registry<HTSyncablePayload.Type<*>> = createRegistry(Keys.SLOT_TYPE)

    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder<T>(key)
        .sync(true)
        .create()

    //    Keys    //

    object Keys {
        @JvmField
        val SLOT_TYPE: ResourceKey<Registry<HTSyncablePayload.Type<*>>> = createKey("syncable_slot_type")

        @JvmStatic
        private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(id(path))
    }
}
