package hiiragi283.core.api

import com.mojang.logging.LogUtils
import hiiragi283.core.api.resource.toId
import net.minecraft.client.Minecraft
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.Identifier
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.player.Player
import net.neoforged.api.distmarker.Dist
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.server.ServerLifecycleHooks
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.callWhenOn
import java.util.ServiceLoader

/**
 * @see mekanism.api.MekanismAPI
 */
data object HiiragiCoreAPI {
    /**
     * Hiiragi CoreのMod ID
     */
    const val MOD_ID = "hiiragi_core"

    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    //    Identifier    //

    /**
     * [名前空間][Identifier.getNamespace]が["hiiragi_core"][MOD_ID]となる[ID][Identifier]を返します。
     * @param path IDの[パス][Identifier.getPath]
     * @return 新しい[Identifier]のインスタンス
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    @JvmStatic
    fun id(path: String): Identifier = MOD_ID.toId(path)

    /**
     * [名前空間][Identifier.getNamespace]が["hiiragi_core"][MOD_ID]となる[ID][Identifier]を返します。
     * @param path IDの[パス][Identifier.getPath]
     * @return [path]が`/`で区切られた新しい[Identifier]のインスタンス
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    @JvmStatic
    fun id(vararg path: String): Identifier = MOD_ID.toId(*path)

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
    fun getActiveAccess(): RegistryAccess? =
        callWhenOn(Dist.CLIENT) { Minecraft.getInstance().level?.registryAccess() } ?: getActiveServer()?.registryAccess()

    /**
     * クラフトを実行している[プレイヤー][Player]のインスタンスを取得します。
     * @return クラフト中のプレイヤーがいない場合は`null`
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
    @Suppress("RedundantNullableReturnType")
    @JvmStatic
    fun getCraftingPlayer(): Player? = CommonHooks.getCraftingPlayer()

    //    Service    //

    /**
     * [ServiceLoader]を通してインスタンスを取得します。
     * @param SERVICE 取得するインスタンスのクラス
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     * @see mekanism.api.MekanismAPI.getService
     */
    @JvmStatic
    internal inline fun <reified SERVICE : Any> getService(): SERVICE =
        ServiceLoader.load(SERVICE::class.java, HiiragiCoreAPI::class.java.classLoader).first()
}
