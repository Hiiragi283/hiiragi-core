package hiiragi283.core

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCCreativeTabs
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForgeMod
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCore {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    init {
        NeoForgeMod.enableMilkFluid()

        val eventBus: IEventBus = MOD_BUS

        eventBus.addListener(::commonSetup)

        HCDataComponents.REGISTER.register(eventBus)

        HCBlocks.register(eventBus)
        HCItems.register(eventBus)
        HCFluids.register(eventBus)

        HCCreativeTabs.REGISTER.register(eventBus)
        HCRecipeSerializers.REGISTER.register(eventBus)
        HCRecipeTypes.REGISTER.register(eventBus)

        LOGGER.info("Hiiragi-Core loaded!")
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
    }
}
