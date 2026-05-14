package hiiragi283.core.common

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCAttachmentTypes
import hiiragi283.core.setup.HCRecipeLookups
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.mod.HTCommonMod
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.registries.NewRegistryEvent

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCore : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        HiiragiCoreAPI.LOGGER.info("Initializing Hiiragi-Core...")

        NeoForgeMod.enableMergedAttributeTooltips()
        NeoForgeMod.enableMilkFluid()

        HCAttachmentTypes.REGISTER.register(eventBus)
        HCRecipeSerializers.REGISTER.register(eventBus)
        HCRecipeTypes.REGISTER.register(eventBus)

        HiiragiCoreAPI.LOGGER.info("Hiiragi-Core initialized")
    }

    override fun registerRegistries(event: NewRegistryEvent) {
        event.register(HTRegistries.ITEM_RESULT_SERIALIZER)
    }

    override fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork(HCRecipeLookups::init)
    }
}
