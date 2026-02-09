package hiiragi283.core.data

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTRootDataGenerator
import hiiragi283.core.data.client.HCTextureProvider
import hiiragi283.core.data.client.lang.HCEnglishLangProvider
import hiiragi283.core.data.client.lang.HCJapaneseLangProvider
import hiiragi283.core.data.client.model.HCBlockStateProvider
import hiiragi283.core.data.client.model.HCItemModelProvider
import hiiragi283.core.data.server.loot.HCBlockLootTableProvider
import hiiragi283.core.data.server.loot.HCGlobalLootModifierProvider
import hiiragi283.core.data.server.loot.HCGlobalLootProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
data object HiiragiCoreDataGen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val (server: HTRootDataGenerator, client: HTRootDataGenerator) = HTRootDataGenerator.withDataPack(event)
        // Server
        server.addLootTables(
            ::HCBlockLootTableProvider to LootContextParamSets.BLOCK,
            HCGlobalLootProvider::EntityProvider to LootContextParamSets.ENTITY,
        )
        server.addProvider(::HCGlobalLootModifierProvider)

        // server.addProvider(::HCRecipeProvider)

        // server.addProvider(::HCFluidTagsProvider)
        // server.addBlockAndItemTags(::HCBlockTagsProvider, ::HCItemTagsProvider)

        // server.addProvider(::HCDataMapProvider)
        // Client
        client.addProvider(::HCEnglishLangProvider)
        client.addProvider(::HCJapaneseLangProvider)

        client.addProvider(::HCTextureProvider)

        client.addProvider(::HCBlockStateProvider)
        client.addProvider(::HCItemModelProvider)
    }
}
