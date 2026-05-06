package hiiragi283.core.common.integration

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.mod.HTCommonMod
import hiiragi283.core.common.integration.ae2.HCAEIntegration
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod

@Mod(HiiragiCoreAPI.MOD_ID)
data object HiiragiCoreIntegration : HTCommonMod() {
    override fun initialize(eventBus: IEventBus, container: ModContainer) {
        if (isLoaded("ae2")) {
            HCAEIntegration.init(eventBus)
        }
    }

    private fun isLoaded(modId: String): Boolean = ModList.get().isLoaded(modId)
}
