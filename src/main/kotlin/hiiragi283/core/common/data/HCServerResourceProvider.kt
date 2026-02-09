package hiiragi283.core.common.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.common.data.map.HCDataMapProviders
import hiiragi283.core.common.data.tag.HCBlockTagsProvider
import hiiragi283.core.common.data.tag.HCFluidTagsProvider
import hiiragi283.core.common.data.tag.HCItemTagsProvider
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
        HiiragiCoreAccess.INSTANCE
            .materialManager
            .keys
            .map(HTIdLike::namespace)
            .distinct()
            .toTypedArray()
            .let(this::addSupportedNamespaces)

        // Tag
        executor.accept(HCBlockTagsProvider)
        executor.accept(HCFluidTagsProvider)
        executor.accept(HCItemTagsProvider)
        // Data Map
        executor.accept(HCDataMapProviders.FurnaceFuels)
    }
}
