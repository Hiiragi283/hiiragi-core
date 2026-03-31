package hiiragi283.core

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCAttachmentTypes
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCCreativeTabs
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCMenuTypes
import hiiragi283.core.setup.HCMiscRegister
import hiiragi283.core.setup.HCRecipeBookCategories
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.core.setup.HCWidgetTypes
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(HiiragiCoreAPI.MOD_ID)
class HiiragiCore(eventBus: IEventBus, container: ModContainer) {
    init {
        HiiragiCoreAPI.LOGGER.info("Hiiragi Core is loading...")

        eventBus.addListener(HCMiscRegister::register)

        HCDataComponents.REGISTER.register(eventBus)

        HCFluids.register(eventBus)
        HCBlocks.register(eventBus)
        HCItems.register(eventBus)

        HCAttachmentTypes.REGISTER.register(eventBus)
        HCBlockEntityTypes.register(eventBus)
        HCCreativeTabs.REGISTER.register(eventBus)
        HCMenuTypes.REGISTER.register(eventBus)
        HCRecipeBookCategories.REGISTER.register(eventBus)
        HCRecipeSerializers.REGISTER.register(eventBus)
        HCRecipeTypes.REGISTER.register(eventBus)
        HCWidgetTypes.REGISTER.register(eventBus)

        HiiragiCoreAPI.LOGGER.info("Hiiragi Core has been loaded successfully!")
    }
}
