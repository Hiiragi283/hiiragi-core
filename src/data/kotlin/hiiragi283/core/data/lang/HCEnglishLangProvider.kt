package hiiragi283.core.data.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import net.minecraft.data.PackOutput

class HCEnglishLangProvider(output: PackOutput) :
    HTLangProvider(output, HiiragiCoreAPI.MOD_ID, HTLangTypes.EN_US),
    HCLangProvider {
    override fun addTranslations() {
        addCommonTranslations(this::add)
        addPatternTranslations(this)
    }
}
