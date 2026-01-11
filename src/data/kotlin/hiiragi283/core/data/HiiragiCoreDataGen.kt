package hiiragi283.core.data

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTRootDataGenerator
import hiiragi283.core.api.function.partially1
import hiiragi283.core.common.material.HTMaterialManagerImpl
import hiiragi283.core.data.client.HCTextureProvider
import hiiragi283.core.data.client.lang.HCEnglishLangProvider
import hiiragi283.core.data.client.lang.HCJapaneseLangProvider
import hiiragi283.core.data.client.model.HCBlockStateProvider
import hiiragi283.core.data.client.model.HCItemModelProvider
import hiiragi283.core.data.server.HCDataMapProvider
import hiiragi283.core.data.server.HCRecipeProvider
import hiiragi283.core.data.server.loot.HCBlockLootTableProvider
import hiiragi283.core.data.server.loot.HCGlobalLootModifierProvider
import hiiragi283.core.data.server.loot.HCGlobalLootProvider
import hiiragi283.core.data.server.tag.HCBlockTagsProvider
import hiiragi283.core.data.server.tag.HCFluidTagsProvider
import hiiragi283.core.data.server.tag.HCItemTagsProvider
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
data object HiiragiCoreDataGen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        HTMaterialManagerImpl.gatherAttributes(true)

        val (server: HTRootDataGenerator, client: HTRootDataGenerator) = HTRootDataGenerator.withDataPack(event)
        // Server
        server.addLootTables(
            ::HCBlockLootTableProvider to LootContextParamSets.BLOCK,
            HCGlobalLootProvider::Entity to LootContextParamSets.ENTITY,
        )
        server.addProvider(::HCGlobalLootModifierProvider)

        server.addProvider(::HCRecipeProvider)

        server.addProvider(::HCFluidTagsProvider)
        val blockTags: CompletableFuture<TagsProvider.TagLookup<Block>> =
            server.addProvider(::HCBlockTagsProvider).contentsGetter()
        server.addProvider(::HCItemTagsProvider.partially1(blockTags))

        server.addProvider(::HCDataMapProvider)
        // Client
        client.addProvider(::HCEnglishLangProvider)
        client.addProvider(::HCJapaneseLangProvider)

        client.addProvider(::HCTextureProvider)

        client.addProvider(::HCBlockStateProvider)
        client.addProvider(::HCItemModelProvider)
    }
}
