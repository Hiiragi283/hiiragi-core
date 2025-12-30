package hiiragi283.core.api

import hiiragi283.core.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.core.api.resource.toId
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.neoforged.neoforge.registries.RegistryBuilder
import net.neoforged.neoforge.server.ServerLifecycleHooks
import java.util.ServiceLoader

/**
 * @see mekanism.api.MekanismAPI
 */
data object HiiragiCoreAPI {
    /**
     * Hiiragi CoreのMod ID
     */
    const val MOD_ID = "hiiragi_core"

    //    ResourceLocation    //

    /**
     * [名前空間][ResourceLocation.getNamespace]が["hiiragi_core"][MOD_ID]となる[ID][ResourceLocation]を返します。
     * @param path IDの[パス][ResourceLocation.getPath]
     * @return 新しい[ResourceLocation]のインスタンス
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    @JvmStatic
    fun id(path: String): ResourceLocation = MOD_ID.toId(path)

    /**
     * [名前空間][ResourceLocation.getNamespace]が["hiiragi_core"][MOD_ID]となる[ID][ResourceLocation]を返します。
     * @param path IDの[パス][ResourceLocation.getPath]
     * @return [path]が`/`で区切られた新しい[ResourceLocation]のインスタンス
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    @JvmStatic
    fun id(vararg path: String): ResourceLocation = MOD_ID.toId(*path)

    //    Server    //

    /**
     * 現在の[サーバー][MinecraftServer]を取得します。
     * @return サーバーがない場合は`null`
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    @JvmStatic
    fun getActiveServer(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()

    /**
     * 現在の[レジストリへのアクセス][RegistryAccess]を取得します。
     * @return レジストリへのアクセスがない場合は`null`
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    @JvmStatic
    fun getActiveAccess(): RegistryAccess? = getActiveServer()?.registryAccess()

    //    Registry    //

    @JvmStatic
    private fun <T : Any> createKey(path: String): ResourceKey<Registry<T>> = ResourceKey.createRegistryKey(id(path))

    @JvmStatic
    private fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>): Registry<T> = RegistryBuilder<T>(key)
        .sync(true)
        .create()

    /**
     * [HTSyncablePayload]の[レジストリキー][ResourceKey]
     * @author Hiiragi Tsubasa
     * @since 0.4.0
     */
    @JvmField
    val SLOT_TYPE_KEY: ResourceKey<Registry<StreamCodec<RegistryFriendlyByteBuf, out HTSyncablePayload>>> = createKey("syncable_slot_type")

    /**
     * [HTSyncablePayload]の[レジストリ][Registry]
     * @author Hiiragi Tsubasa
     * @since 0.4.0
     */
    @JvmField
    val SLOT_TYPE_REGISTRY: Registry<StreamCodec<RegistryFriendlyByteBuf, out HTSyncablePayload>> = createRegistry(SLOT_TYPE_KEY)

    //    Service    //

    /**
     * [ServiceLoader]を通してインスタンスを取得します。
     * @param SERVICE 取得するインスタンスのクラス
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     * @see mekanism.api.MekanismAPI.getService
     */
    @Suppress("UnstableApiUsage")
    @JvmStatic
    internal inline fun <reified SERVICE : Any> getService(): SERVICE =
        ServiceLoader.load(SERVICE::class.java, HiiragiCoreAPI::class.java.classLoader).first()
}
