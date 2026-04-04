package hiiragi283.core.data

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.createLootProvider
import hiiragi283.core.api.data.createRecipeProvider
import hiiragi283.core.data.lang.HCEnglishLangProvider
import hiiragi283.core.data.lang.HCJapaneseLangProvider
import hiiragi283.core.data.loot.HCBlockLootTableProvider
import hiiragi283.core.data.model.HCModelProvider
import hiiragi283.core.data.recipe.HCRecipeProvider
import hiiragi283.core.data.recipe.HCVanillaRecipeProvider
import hiiragi283.core.data.tag.HCBlockTagsProvider
import hiiragi283.core.data.tag.HCFluidTagsProvider
import hiiragi283.core.data.tag.HCItemTagsProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
object HiiragiCoreDataGen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent.Client) {
        // Server
        event.createLootProvider(::HCBlockLootTableProvider to LootContextParamSets.BLOCK)

        event.createRecipeProvider("Recipes for Vanilla", ::HCVanillaRecipeProvider)
        event.createRecipeProvider("Recipes for Hiiragi Core", ::HCRecipeProvider)

        event.createProvider(::HCFluidTagsProvider)
        event.createBlockAndItemTags(::HCBlockTagsProvider, ::HCItemTagsProvider)
        // Client
        event.createProvider(::HCEnglishLangProvider)
        event.createProvider(::HCJapaneseLangProvider)

        event.createProvider(::HCModelProvider)
    }
}
