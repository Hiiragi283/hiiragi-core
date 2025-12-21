package hiiragi283.core.api

import hiiragi283.core.api.resource.toId
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
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
     * [名前空間][ResourceLocation.getNamespace]が["hiiragi_core"][MOD_ID]となる[ResourceLocation]を返します。
     * @param path [ResourceLocation]の[パス][ResourceLocation.getPath]
     * @return 新しい[ResourceLocation]のインスタンス
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    @JvmStatic
    fun id(path: String): ResourceLocation = MOD_ID.toId(path)

    /**
     * [名前空間][ResourceLocation.getNamespace]が["hiiragi_core"][MOD_ID]となる[ResourceLocation]を返します。
     * @param path [ResourceLocation]の[パス][ResourceLocation.getPath]
     * @return [path]が`/`で区切られた新しい[ResourceLocation]のインスタンス
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    @JvmStatic
    fun id(vararg path: String): ResourceLocation = MOD_ID.toId(*path)

    //    Server    //

    /**
     * 稼働中の[MinecraftServer]を取得します。
     * @return 稼働中の[MinecraftServer]がない場合は`null`
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    @JvmStatic
    fun getActiveServer(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()

    /**
     * 稼働中の[RegistryAccess]を取得します。
     * @return 稼働中の[RegistryAccess]がない場合は`null`
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    @JvmStatic
    fun getActiveAccess(): RegistryAccess? = getActiveServer()?.registryAccess()

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
