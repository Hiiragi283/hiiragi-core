package hiiragi283.core

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCore {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    init {
        val eventBus: IEventBus = MOD_BUS

        HCRecipeSerializers.REGISTER.register(eventBus)
        HCRecipeTypes.REGISTER.register(eventBus)

        LOGGER.info("Hiiragi-Core Loaded!")
    }
}
