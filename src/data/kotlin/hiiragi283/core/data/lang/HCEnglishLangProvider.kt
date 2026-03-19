package hiiragi283.core.data.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.lang.HTLanguageProvider
import hiiragi283.core.setup.HCBlocks
import net.minecraft.data.PackOutput

class HCEnglishLangProvider(output: PackOutput) : HTLanguageProvider.English(output, HiiragiCoreAPI.MOD_ID) {
    override fun addTranslations() {
        // Block
        add(HCBlocks.WARPED_WART, "Warped Wart")
    }
}
