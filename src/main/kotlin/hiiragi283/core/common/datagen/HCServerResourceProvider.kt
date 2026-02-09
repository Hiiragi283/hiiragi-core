package hiiragi283.core.common.datagen

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.common.datagen.loot.HCMaterialBlockLootProvider
import hiiragi283.core.common.datagen.map.HCDataMapProviders
import hiiragi283.core.common.datagen.recipe.HCChargingRecipeProvider
import hiiragi283.core.common.datagen.recipe.HCCommonRecipeProvider
import hiiragi283.core.common.datagen.recipe.HCCrushingRecipeProvider
import hiiragi283.core.common.datagen.recipe.HCExplodingRecipeProvider
import hiiragi283.core.common.datagen.tag.HCBlockTagsProvider
import hiiragi283.core.common.datagen.tag.HCFluidTagsProvider
import hiiragi283.core.common.datagen.tag.HCItemTagsProvider
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import java.util.function.Consumer

data object HCServerResourceProvider : HTDynamicResourceProvider.Server(HiiragiCoreAPI.MOD_ID) {
    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        HTDynamicResourceProvider.addMaterialIds(this::addSupportedNamespaces)

        // Data Map
        executor.accept(HCDataMapProviders.FurnaceFuels)
        // Loot Table
        executor.accept(HCMaterialBlockLootProvider)
        // Recipe
        executor.accept(HCChargingRecipeProvider)
        executor.accept(HCCommonRecipeProvider)
        executor.accept(HCCrushingRecipeProvider)
        executor.accept(HCExplodingRecipeProvider)
        // Tag
        executor.accept(HCBlockTagsProvider)
        executor.accept(HCFluidTagsProvider)
        executor.accept(HCItemTagsProvider)
    }
}
