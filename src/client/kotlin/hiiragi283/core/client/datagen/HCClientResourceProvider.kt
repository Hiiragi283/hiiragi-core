package hiiragi283.core.client.datagen

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.client.datagen.lang.HCEnglishLangProvider
import hiiragi283.core.client.datagen.lang.HCJapaneseLangProvider
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import java.util.function.Consumer

data object HCClientResourceProvider : HTDynamicResourceProvider.Client(HiiragiCoreAPI.MOD_ID) {
    override fun addDynamicTranslations(afterLanguageLoadEvent: AfterLanguageLoadEvent) {}

    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        HTDynamicResourceProvider.addMaterialIds(this::addSupportedNamespaces)

        // Lang
        executor.accept(HCEnglishLangProvider)
        executor.accept(HCJapaneseLangProvider)
    }
}
