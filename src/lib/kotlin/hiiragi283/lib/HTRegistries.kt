package hiiragi283.lib

import hiiragi283.lib.gui.sync.HTSyncablePayload
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.resource.toId
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

/**
 * Hiiragi Seriesで使用される[レジストリ][Registry]を保持するクラスです。
 * @author Hiiragi Tsubasa
 */
data object HTRegistries {
    @JvmField
    val ITEM_RESULT_SERIALIZER: Registry<HTItemResult.Serializer<*>> = createRegistry(Keys.ITEM_RESULT_SERIALIZER)

    @JvmField
    val SLOT_TYPE: Registry<HTSyncablePayload.Type<*>> = createRegistry(Keys.SLOT_TYPE)

    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder(key)
        .sync(true)
        .create()

    //    Keys    //

    /**
     * Hiiragi Seriesで使用される[レジストリ][Registry]の[キー][ResourceKey]を保持するクラスです。
     * @author Hiiragi Tsubasa
     */
    object Keys {
        @JvmField
        val ITEM_RESULT_SERIALIZER: ResourceKey<Registry<HTItemResult.Serializer<*>>> = createKey("item_result_serializer")

        @JvmField
        val SLOT_TYPE: ResourceKey<Registry<HTSyncablePayload.Type<*>>> = createKey("syncable_slot_type")

        @JvmStatic
        private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(HTConstants.MOD_ID.toId(path))
    }
}
