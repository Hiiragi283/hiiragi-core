package hiiragi283.core.data

import hiiragi283.core.data.lang.HCEnglishLangProvider
import hiiragi283.core.data.lang.HCJapaneseLangProvider
import hiiragi283.core.data.loot.HCBlockLootTableProvider
import hiiragi283.core.data.model.HCModelProvider
import hiiragi283.core.data.recipe.HCChargingRecipeProvider
import hiiragi283.core.data.recipe.HCExplodingRecipeProvider
import hiiragi283.core.data.recipe.HCMaterialRecipeProvider
import hiiragi283.core.data.recipe.HCVanillaRecipeProvider
import hiiragi283.core.data.tag.HCBlockTagsProvider
import hiiragi283.core.data.tag.HCFluidTagsProvider
import hiiragi283.core.data.tag.HCItemTagsProvider
import hiiragi283.core.data.tag.HCMaterialContentsTagsProvider
import hiiragi283.lib.data.createLootTables
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber
data object HiiragiCoreData {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent.Client) {
        // Server
        event.createProvider(::HCDataMapProvider)

        event.createLootTables(::HCBlockLootTableProvider to LootContextParamSets.BLOCK)

        event.createProvider(::HCFluidTagsProvider)
        event.createProvider(::HCMaterialContentsTagsProvider)
        event.createBlockAndItemTags(::HCBlockTagsProvider, ::HCItemTagsProvider)

        event.createProvider(HCChargingRecipeProvider::Runner)
        event.createProvider(HCExplodingRecipeProvider::Runner)
        event.createProvider(HCMaterialRecipeProvider::Runner)
        event.createProvider(HCVanillaRecipeProvider::Runner)
        // Client
        event.createProvider(::HCModelProvider)

        event.createProvider(::HCEnglishLangProvider)
        event.createProvider(::HCJapaneseLangProvider)
    }
}
