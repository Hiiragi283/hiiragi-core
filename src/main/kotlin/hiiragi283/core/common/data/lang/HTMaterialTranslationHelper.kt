package hiiragi283.core.common.data.lang

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.common.material.HCMaterialPrefixes
import kotlin.collections.component1
import kotlin.collections.component2

object HTMaterialTranslationHelper {
    @JvmStatic
    fun translateAll(provider: HTLangProvider, entryProvider: (HTMaterialKey) -> Map<HTMaterialPrefix, HTHasTranslationKey>) {
        val langType: HTLanguageType = provider.langType
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in HTMaterialManager.INSTANCE.entries) {
            for ((prefix: HTMaterialPrefix, translation: HTHasTranslationKey) in entryProvider(key)) {
                val name: String = translate(langType, prefix, propertyMap) ?: continue
                provider.add(translation, name)
            }
        }
    }

    @JvmStatic
    fun translate(type: HTLanguageType, prefix: HTMaterialPrefix, propertyMap: HTPropertyMap): String? =
        propertyMap.getOrDefault(HTMaterialPropertyKeys.CUSTOM_LANG_NAME)[prefix]?.getTranslatedName(type) ?: run {
            val materialName: HTLangName = propertyMap[HTMaterialPropertyKeys.LANG_NAME] ?: return@run null
            HCMaterialPrefixes.TRANSLATION_MAP[prefix]?.translate(type, materialName)
        }
}
