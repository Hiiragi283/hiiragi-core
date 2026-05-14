package hiiragi283.core.data

import hiiragi283.core.data.lang.HCEnglishLangProvider
import hiiragi283.core.data.lang.HCJapaneseLangProvider
import hiiragi283.core.data.recipe.HCChargingRecipeProvider
import hiiragi283.core.data.recipe.HCExplodingRecipeProvider
import hiiragi283.core.data.recipe.HCVanillaRecipeProvider
import hiiragi283.core.data.tag.HCBlockTagsProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber
data object HiiragiCoreData {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent.Client) {
        // Server
        event.createProvider(::HCDataMapProvider)

        event.createProvider(::HCBlockTagsProvider)

        event.createProvider(HCChargingRecipeProvider::Runner)
        event.createProvider(HCExplodingRecipeProvider::Runner)
        event.createProvider(HCVanillaRecipeProvider::Runner)
        // Client
        event.createProvider(::HCEnglishLangProvider)
        event.createProvider(::HCJapaneseLangProvider)
    }
}
