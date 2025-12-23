package hiiragi283.core.common.data.lang

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.common.material.HCMaterialPrefixes
import kotlin.collections.get

object HTMaterialTranslationHelper {
    @JvmStatic
    fun translate(type: HTLanguageType, prefix: HTPrefixLike, material: HTMaterialLike): String? {
        val translation: HTLangPatternProvider = HCMaterialPrefixes.TRANSLATION_MAP[prefix] ?: return null
        val translatedName: HTLangName = material as? HTLangName ?: return null
        return translation.translate(type, translatedName)
    }
}
