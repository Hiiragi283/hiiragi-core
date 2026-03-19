package hiiragi283.core

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCCreativeTabs
import hiiragi283.core.setup.HCMiscRegister
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(HiiragiCoreAPI.MOD_ID)
class HiiragiCore(eventBus: IEventBus, container: ModContainer) {
    init {
        HiiragiCoreAPI.LOGGER.info("Hiiragi Core is loading...")

        eventBus.addListener(HCMiscRegister::register)

        HCBlocks.register(eventBus)

        HCCreativeTabs.REGISTER.register(eventBus)

        HiiragiCoreAPI.LOGGER.info("Hiiragi Core has been loaded successfully!")
    }
}
