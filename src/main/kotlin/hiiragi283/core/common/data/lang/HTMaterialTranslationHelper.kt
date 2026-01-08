package hiiragi283.core.common.data.lang

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.attribute.HTLangNameMaterialAttribute
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.material.HCMaterialPrefixes

object HTMaterialTranslationHelper {
    @JvmStatic
    fun translate(
        type: HTLanguageType,
        prefix: HTMaterialPrefix,
        key: HTMaterialKey,
        definition: HTMaterialDefinition,
        customName: (HTMaterialPrefix, HTMaterialKey) -> HTLangName?,
    ): String? = customName(prefix, key)?.getTranslatedName(type) ?: run {
        val materialName: HTLangName = definition.get<HTLangNameMaterialAttribute>() ?: return@run null
        HCMaterialPrefixes.TRANSLATION_MAP[prefix]?.translate(type, materialName)
    }
}
