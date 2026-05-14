package hiiragi283.core.data

import hiiragi283.core.data.recipe.HCChargingRecipeProvider
import hiiragi283.core.data.recipe.HCExplodingRecipeProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber
data object HiiragiCoreData {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent.Client) {
        // Server
        event.createProvider(HCChargingRecipeProvider::Runner)
        event.createProvider(HCExplodingRecipeProvider::Runner)
        // Client
    }
}
