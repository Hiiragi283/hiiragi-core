package hiiragi283.core.data.client.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCFluids
import net.minecraft.data.PackOutput

class HCJapaneseLangProvider(output: PackOutput) : HTLangProvider.Japanese(output, HiiragiCoreAPI.MOD_ID) {
    override fun addTranslations() {
        // Material
        HCMaterialTranslations.addTranslations(this)

        // Fluid
        addFluid(HCFluids.HONEY, "ハチミツ")
        addFluid(HCFluids.MUSHROOM_STEW, "キノコシチュー")

        // Translation
        add(HCTranslation.CREATIVE_TAB_MATERIAL, "Hiiragi Core - 素材")
        add(HCTranslation.CREATIVE_TAB_TOOL, "Hiiragi Core - 道具")
    }
}
