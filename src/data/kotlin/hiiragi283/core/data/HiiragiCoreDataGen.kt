package hiiragi283.core.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.createLootTables
import hiiragi283.core.api.data.createProviderWithHelper
import hiiragi283.core.api.function.partially1
import hiiragi283.core.data.bootsrap.HCEnchantmentProvider
import hiiragi283.core.data.lang.HCEnglishLangProvider
import hiiragi283.core.data.lang.HCJapaneseLangProvider
import hiiragi283.core.data.loot.HCBlockLootTableProvider
import hiiragi283.core.data.loot.HCGlobalLootModifierProvider
import hiiragi283.core.data.loot.HCGlobalLootProvider
import hiiragi283.core.data.model.HCBlockStateProvider
import hiiragi283.core.data.model.HCItemModelProvider
import hiiragi283.core.data.tag.HCBlockTagsProvider
import hiiragi283.core.data.tag.HCDamageTypeTagsProvider
import hiiragi283.core.data.tag.HCEntityTypeTagsProvider
import hiiragi283.core.data.tag.HCFluidTagsProvider
import hiiragi283.core.data.tag.HCItemTagsProvider
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
data object HiiragiCoreDataGen {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val fileHelper: ExistingFileHelper = event.existingFileHelper

        event.createDatapackRegistryObjects(
            RegistrySetBuilder()
                .add(Registries.ENCHANTMENT, HCEnchantmentProvider),
            setOf(event.modContainer.modId, HTConst.MINECRAFT),
        )
        // Server
        event.createLootTables(
            ::HCBlockLootTableProvider to LootContextParamSets.BLOCK,
            HCGlobalLootProvider::EntityProvider to LootContextParamSets.ENTITY,
        )
        event.createProvider(::HCGlobalLootModifierProvider)

        event.createProvider(::HCRecipeProvider)

        event.createProviderWithHelper(::HCDamageTypeTagsProvider)
        event.createProviderWithHelper(::HCEntityTypeTagsProvider)
        event.createProviderWithHelper(::HCFluidTagsProvider)
        event.createBlockAndItemTags(::HCBlockTagsProvider.partially1(fileHelper), ::HCItemTagsProvider.partially1(fileHelper))

        event.createProvider(::HCDataMapProvider)
        // Client
        event.createProvider(::HCEnglishLangProvider)
        event.createProvider(::HCJapaneseLangProvider)

        event.createProviderWithHelper(::HCBlockStateProvider)
        event.createProviderWithHelper(::HCItemModelProvider)
    }
}
