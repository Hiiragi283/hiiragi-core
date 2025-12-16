package hiiragi283.core.data.client.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCFluids
import net.minecraft.data.PackOutput

class HCEnglishLangProvider(output: PackOutput) : HTLangProvider.English(output, HiiragiCoreAPI.MOD_ID) {
    override fun addTranslations() {
        // Material
        HCMaterialTranslations.addTranslations(this)

        // Fluid
        addFluid(HCFluids.HONEY, "Honey")
        addFluid(HCFluids.MUSHROOM_STEW, "Mushroom Stew")

        // Translation
        add(HCTranslation.CREATIVE_TAB_MATERIAL, "Hiiragi Core - Material")
        add(HCTranslation.CREATIVE_TAB_TOOL, "Hiiragi Core - Tool")
    }
}
