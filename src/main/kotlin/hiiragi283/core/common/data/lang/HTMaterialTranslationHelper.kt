package hiiragi283.core.common.data.lang

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.common.material.HCMaterialPrefixes

object HTMaterialTranslationHelper {
    @JvmStatic
    fun translate(
        type: HTLanguageType,
        prefix: HTMaterialPrefix,
        key: HTMaterialKey,
        propertyMap: HTPropertyMap,
        customName: (HTMaterialPrefix, HTMaterialKey) -> HTLangName?,
    ): String? = customName(prefix, key)?.getTranslatedName(type) ?: run {
        val materialName: HTLangName = propertyMap[HTMaterialPropertyKeys.LANG_NAME] ?: return@run null
        HCMaterialPrefixes.TRANSLATION_MAP[prefix]?.translate(type, materialName)
    }
}
