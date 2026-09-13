package hiiragi283.core.client.integration

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.pack.HTDynamicResourcePack
import hiiragi283.core.api.mod.HTClientMod
import hiiragi283.core.common.integration.HCIConstants
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(value = HiiragiCoreAPI.MOD_ID, dist = [Dist.CLIENT])
data object HiiragiCoreIntegrationClient : HTClientMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        HTDynamicResourcePack.addDomain(HCIConstants.AE2)
        HTDynamicResourcePack.addDomain(HCIConstants.REPLICATION)
    }
}
