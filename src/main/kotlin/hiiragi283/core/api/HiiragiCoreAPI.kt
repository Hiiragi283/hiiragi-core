package hiiragi283.core.api

import com.mojang.logging.LogUtils
import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.toId
import net.minecraft.resources.Identifier
import net.minecraft.world.flag.FeatureFlag
import net.minecraft.world.flag.FeatureFlags
import org.slf4j.Logger

data object HiiragiCoreAPI {
    /**
     * Hiiragi CoreのMod ID
     */
    const val MOD_ID = "hiiragi_core"

    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val EXPERIMENTAL: FeatureFlag = FeatureFlags.REGISTRY.getFlag(id(HTConstants.EXPERIMENTAL))

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
}
