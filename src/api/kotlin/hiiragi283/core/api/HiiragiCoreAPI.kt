package hiiragi283.core.api

import hiiragi283.core.api.resource.toId
import net.minecraft.resources.ResourceLocation
import java.util.ServiceLoader

data object HiiragiCoreAPI {
    const val MOD_ID = "hiiragi_core"

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

    //    Service    //

    /**
     * @see mekanism.api.MekanismAPI.getService
     */
    @Suppress("UnstableApiUsage")
    @JvmStatic
    inline fun <reified SERVICE : Any> getService(): SERVICE =
        ServiceLoader.load(SERVICE::class.java, HiiragiCoreAPI::class.java.classLoader).first()
}
