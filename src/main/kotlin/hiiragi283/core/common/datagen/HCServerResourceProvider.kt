package hiiragi283.core.common.datagen

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.common.datagen.map.HCDataMapProviders
import hiiragi283.core.common.datagen.recipe.HCChargingRecipeProvider
import hiiragi283.core.common.datagen.recipe.HCCommonRecipeProvider
import hiiragi283.core.common.datagen.recipe.HCCrushingRecipeProvider
import hiiragi283.core.common.datagen.recipe.HCExplodingRecipeProvider
import hiiragi283.core.common.datagen.tag.HCBlockTagsProvider
import hiiragi283.core.common.datagen.tag.HCFluidTagsProvider
import hiiragi283.core.common.datagen.tag.HCItemTagsProvider
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicServerResourceProvider
import net.mehvahdjukaar.moonlight.api.resources.pack.PackGenerationStrategy
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import java.util.function.Consumer

data object HCServerResourceProvider :
    DynamicServerResourceProvider(HiiragiCoreAPI.id("dynamic_resources"), PackGenerationStrategy.REGEN_ON_EVERY_RELOAD) {
    override fun gatherSupportedNamespaces(): Collection<String> = buildSet {
        this += HTConst.MINECRAFT
        this += HTConst.COMMON
        this += HTConst.NEOFORGE
    }

    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        HiiragiCoreAccess.Companion.INSTANCE
            .materialManager
            .keys
            .map(HTIdLike::namespace)
            .distinct()
            .toTypedArray()
            .let(this::addSupportedNamespaces)

        // Data Map
        executor.accept(HCDataMapProviders.FurnaceFuels)
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
