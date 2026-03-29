package hiiragi283.core.client

import hiiragi283.core.api.HiiragiCoreAPI
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent

@Mod(value = HiiragiCoreAPI.MOD_ID, dist = [Dist.CLIENT])
class HiiragiCoreClient(eventBus: IEventBus, container: ModContainer) {
    init {
        eventBus.addListener(::registerFluidModels)

        HiiragiCoreAPI.LOGGER.info("Hiiragi Core Client has been loaded successfully!")
    }

    private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
    }

    private fun registerFluidModels(event: RegisterFluidModelsEvent) {
    }
}
