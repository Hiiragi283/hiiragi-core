package hiiragi283.core.common.integration.ae2

import appeng.api.AECapabilities
import hiiragi283.core.common.integration.ae2.storage.HTFluidTankMEStorage
import hiiragi283.core.setup.HCBlockEntityTypes
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent

data object HCAEIntegration {
    @JvmStatic
    internal fun init(eventBus: IEventBus) {
        eventBus.addListener(::registerMeStorage)
    }

    @JvmStatic
    private fun registerMeStorage(event: RegisterCapabilitiesEvent) {
        event.registerBlockEntity(AECapabilities.ME_STORAGE, HCBlockEntityTypes.COPPER_BASIN.get()) { blockEntity, _ ->
            HTFluidTankMEStorage(blockEntity.tank, blockEntity.name)
        }
    }
}
