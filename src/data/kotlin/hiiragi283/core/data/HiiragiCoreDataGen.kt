package hiiragi283.core.data

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTRootDataGenerator
import hiiragi283.core.data.client.HCItemModelProvider
import hiiragi283.core.data.server.HCDataMapProvider
import hiiragi283.core.data.server.HCRecipeProvider
import hiiragi283.core.data.server.tag.HCItemTagsProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
data object HiiragiCoreDataGen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val (server: HTRootDataGenerator, client: HTRootDataGenerator) = HTRootDataGenerator.withDataPack(event)
        // Server
        server.addProvider(::HCRecipeProvider)

        server.addProvider(::HCItemTagsProvider)

        server.addProvider(::HCDataMapProvider)
        // Client
        client.addProvider(::HCItemModelProvider)
    }
}
