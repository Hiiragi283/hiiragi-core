package hiiragi283.core.client.datagen

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.api.text.HTHasTranslationKey
import kotlin.collections.iterator

object HCMaterialTranslationHelper {
    @JvmStatic
    fun addTranslations(langType: HTLangType, consumer: (HTHasTranslationKey, String) -> Unit) {
        val contents: HTMaterialContents = HiiragiCoreAccess.Companion.INSTANCE.materialContents
        for (entry: HTMaterialManager.Entry in HiiragiCoreAccess.Companion.INSTANCE.materialManager) {
            // Block
            for ((prefix: HTTagPrefix, block: HTHasTranslationKey) in contents.getBlockMap(entry)) {
                val name: String = translate(langType, prefix, entry) ?: continue
                consumer(block, name)
            }
            // Item
            for ((prefix: HTTagPrefix, item: HTHasTranslationKey) in contents.getItemMap(entry)) {
                val name: String = translate(langType, prefix, entry) ?: continue
                consumer(item, name)
            }
            // Tool
            for ((toolType: HTToolType, tool: HTHasTranslationKey) in contents.getToolMap(entry)) {
                val materialName: HTLangName = entry[HTMaterialPropertyKeys.LANG_NAME] ?: continue
                consumer(tool, toolType.langPattern.translate(langType, materialName))
            }
        }
    }

    @JvmStatic
    private fun translate(type: HTLangType, prefix: HTTagPrefix, propertyMap: HTPropertyMap): String? =
        propertyMap.getOrDefault(HTMaterialPropertyKeys.CUSTOM_LANG_NAME)[prefix]?.getTranslatedName(type) ?: run {
            val materialName: HTLangName = propertyMap[HTMaterialPropertyKeys.LANG_NAME] ?: return@run null
            prefix.getOrDefault(HTTagPropertyKeys.LANG_PATTERN).translate(type, materialName)
        }
}
