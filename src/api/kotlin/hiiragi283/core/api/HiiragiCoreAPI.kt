package hiiragi283.core.api

import com.mojang.logging.LogUtils
import hiiragi283.core.api.resource.toId
import java.util.ServiceLoader
import kotlin.random.Random
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.flag.FeatureFlag
import net.minecraft.world.flag.FeatureFlags
import net.neoforged.neoforge.common.CommonHooks
import org.slf4j.Logger

data object HiiragiCoreAPI {
    /**
     * Hiiragi CoreのMod ID
     */
    const val MOD_ID = "hiiragi_core"

    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    /**
     * @since 0.15.0
     */
    @JvmField
    val RANDOM: Random = Random.Default

    @JvmField
    val EXPERIMENTAL: FeatureFlag = FeatureFlags.REGISTRY.getFlag(id(HTConst.EXPERIMENTAL))

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
     * クラフトを実行している[プレイヤー][Player]のインスタンスを取得します。
     * @return クラフト中のプレイヤーがいない場合は`null`
     * @author Hiiragi Tsubasa
     * @since 0.10.0
     */
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
    @Suppress("UnstableApiUsage")
    @JvmStatic
    inline fun <reified SERVICE : Any> getService(): SERVICE = ServiceLoader.load(SERVICE::class.java, HiiragiCoreAPI::class.java.classLoader).single()
}
