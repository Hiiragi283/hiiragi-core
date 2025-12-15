package hiiragi283.core.data

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTRootDataGenerator
import hiiragi283.core.data.server.HCRecipeProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
data object HiiragiCoreDataGen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val (server: HTRootDataGenerator, client: HTRootDataGenerator) = HTRootDataGenerator.withDataPack(event)

        server.addProvider(::HCRecipeProvider)
    }
}
