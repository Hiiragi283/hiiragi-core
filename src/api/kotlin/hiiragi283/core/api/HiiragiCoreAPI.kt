package hiiragi283.core.api

import com.mojang.logging.LogUtils
import hiiragi283.core.api.resource.toId
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger

data object HiiragiCoreAPI {
    const val MOD_ID = "hiiragi_core"

    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    //    ResourceLocation    //

    /**
     * 名前空間が`ragium`となる[ResourceLocation]を返します。
     */
    @JvmStatic
    fun id(path: String): ResourceLocation = MOD_ID.toId(path)

    /**
     * 名前空間が`ragium`となる[ResourceLocation]を返します。
     */
    @JvmStatic
    fun id(vararg path: String): ResourceLocation = MOD_ID.toId(*path)
}
