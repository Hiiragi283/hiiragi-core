package hiiragi283.core.common.integration

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.mod.HTCommonMod
import hiiragi283.core.common.data.HCServerResourceProvider
import hiiragi283.core.common.integration.ae2.HCAEIntegration
import hiiragi283.core.common.integration.immersive.HCIEIntegration
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCoreIntegration : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        HCServerResourceProvider.addSupportedNamespaces(HCIConstants.AE2)

        if (HCIConstants.isLoaded(HCIConstants.AE2)) {
            HCAEIntegration.init(eventBus)
        }
        if (HCIConstants.isLoaded(HCIConstants.IMMERSIVE)) {
            HCIEIntegration.init(eventBus)
        }
    }
}
