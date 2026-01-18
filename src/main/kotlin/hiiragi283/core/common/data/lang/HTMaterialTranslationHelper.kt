package hiiragi283.core.common.data.lang

import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.setup.HCMiscRegister

object HTMaterialTranslationHelper {
    @JvmStatic
    fun translateAll(provider: HTLangProvider) {
        val modId: String = provider.modId
        val langType: HTLanguageType = provider.langType
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in HTMaterialManager.INSTANCE.entries) {
            if (key.getNamespace() != modId) continue
            // Block
            for ((prefix: HTTagPrefix, item: HTHasTranslationKey) in HCMiscRegister.materialBlocks.column(key)) {
                val name: String = translate(langType, prefix, propertyMap) ?: continue
                provider.add(item, name)
            }
            // Item
            for ((prefix: HTTagPrefix, item: HTHasTranslationKey) in HCMiscRegister.materialItems.column(key)) {
                val name: String = translate(langType, prefix, propertyMap) ?: continue
                provider.add(item, name)
            }
        }
    }

    @JvmStatic
    fun translate(type: HTLanguageType, prefix: HTTagPrefix, propertyMap: HTPropertyMap): String? =
        propertyMap.getOrDefault(HTMaterialPropertyKeys.CUSTOM_LANG_NAME)[prefix]?.getTranslatedName(type) ?: run {
            val materialName: HTLangName = propertyMap[HTMaterialPropertyKeys.LANG_NAME] ?: return@run null
            prefix.getOrDefault(HTTagPropertyKeys.LANG_PATTERN).translate(type, materialName)
        }
}
