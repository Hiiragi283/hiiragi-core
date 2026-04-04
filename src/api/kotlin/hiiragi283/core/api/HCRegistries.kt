package hiiragi283.core.api

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HiiragiCoreAPI.id
import hiiragi283.core.api.gui.sync.HTSyncablePayload
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.material.HTMaterial
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

/**
 * Hiiragi Coreで追加される[レジストリ][Registry]を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
object HCRegistries {
    @Suppress("DEPRECATION")
    @JvmField
    val MATERIAL: Registry<HTMaterial> = RegistryBuilder(Keys.MATERIAL)
        .sync(true)
        .withIntrusiveHolders()
        .create()

    @JvmField
    val MATERIAL_COMPONENT_TYPE: Registry<DataComponentType<*>> = createRegistry(Keys.MATERIAL_COMPONENT_TYPE)

    @JvmField
    val SLOT_TYPE: Registry<HTSyncablePayload.Type<*>> = createRegistry(Keys.SLOT_TYPE)

    // val TANK_INTERACTION_TYPE: Registry<MapCodec<out HTTankInteraction.Serializable>> = createRegistry(Keys.TANK_INTERACTION_TYPE)

    @JvmField
    val WIDGET_TYPE: Registry<HTWidgetType<*>> = createRegistry(Keys.WIDGET_TYPE)

    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder(key)
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
        val MATERIAL: ResourceKey<Registry<HTMaterial>> = createKey(HTConst.MATERIAL)

        @JvmField
        val MATERIAL_COMPONENT_TYPE: ResourceKey<Registry<DataComponentType<*>>> = createKey("material_component_type")

        @JvmField
        val SLOT_TYPE: ResourceKey<Registry<HTSyncablePayload.Type<*>>> = createKey("syncable_slot_type")

        // val TANK_INTERACTION_TYPE: ResourceKey<Registry<MapCodec<out HTTankInteraction.Serializable>>> = createCodecKey(HTConst.TANK_INTERACTION)

        @JvmField
        val WIDGET_TYPE: ResourceKey<Registry<HTWidgetType<*>>> = createKey("widget_type")

        @JvmStatic
        private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(id(path))

        @JvmStatic
        private fun <T : Any> createCodecKey(path: String): ResourceKey<Registry<MapCodec<out T>>> = ResourceKey.createRegistryKey(id(path))
    }
}
