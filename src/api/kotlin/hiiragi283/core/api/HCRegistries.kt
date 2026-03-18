package hiiragi283.core.api

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HiiragiCoreAPI.id
import hiiragi283.core.api.data.tank.HTTankInteraction
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.gui.widget.HTWidgetType
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

/**
 * Hiiragi Coreで追加される[レジストリ][Registry]を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
object HCRegistries {
    @JvmField
    val SLOT_TYPE: Registry<HTSyncablePayload.Type<*>> = createRegistry(Keys.SLOT_TYPE)

    @JvmField
    val TANK_INTERACTION_TYPE: Registry<MapCodec<out HTTankInteraction.Serializable>> = createRegistry(Keys.TANK_INTERACTION_TYPE)

    @JvmField
    val WIDGET_TYPE: Registry<HTWidgetType<*>> = createRegistry(Keys.WIDGET_TYPE)

    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder<T>(key)
        .sync(true)
        .create()

    //    Keys    //

    /**
     * Hiiragi Coreで追加される[レジストリ][Registry]の[キー][ResourceKey]を保持するクラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    object Keys {
        @JvmField
        val SLOT_TYPE: ResourceKey<Registry<HTSyncablePayload.Type<*>>> = createKey("syncable_slot_type")

        @JvmField
        val TANK_INTERACTION_TYPE: ResourceKey<Registry<MapCodec<out HTTankInteraction.Serializable>>> =
            createCodecKey(HTConst.TANK_INTERACTION)

        @JvmField
        val WIDGET_TYPE: ResourceKey<Registry<HTWidgetType<*>>> = createKey("widget_type")

        @JvmStatic
        private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(id(path))

        @JvmStatic
        private fun <T : Any> createCodecKey(path: String): ResourceKey<Registry<MapCodec<out T>>> = ResourceKey.createRegistryKey(id(path))
    }
}
