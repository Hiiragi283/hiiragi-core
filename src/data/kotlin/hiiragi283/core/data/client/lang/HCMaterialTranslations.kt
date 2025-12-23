package hiiragi283.core.data.client.lang

import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.common.data.lang.HTMaterialTranslationHelper
import hiiragi283.core.common.item.HTToolType
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems

object HCMaterialTranslations {
    @JvmStatic
    val MATERIAL_MAP: HTMaterialTable<HTMaterialPrefix, HTLangName> = buildTable {
        fun register(
            prefix: HTPrefixLike,
            material: HTMaterialLike,
            enName: String,
            jaName: String,
        ) {
            this[prefix.asMaterialPrefix(), material.asMaterialKey()] = HTLangName { type: HTLanguageType ->
                when (type) {
                    HTLanguageType.EN_US -> enName
                    HTLanguageType.JA_JP -> jaName
                }
            }
        }

        register(HCMaterialPrefixes.DUST, HCMaterial.Wood, "Sawdust", "おがくず")
        register(HCMaterialPrefixes.PLATE, HCMaterial.Plates.RUBBER, "Rubber Sheet", "ゴムシート")
        register(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.PLASTIC, "Polymer Resin", "高分子樹脂")
        register(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER, "Raw Rubber", "生ゴム")
    }.let(::HTMaterialTable)

    @JvmStatic
    fun addTranslations(provider: HTLangProvider) {
        val langType: HTLanguageType = provider.langType
        for (material: HCMaterial in HCMaterial.entries) {
            // Block
            for ((prefix: HTMaterialPrefix, block: HTHasTranslationKey) in HCBlocks.MATERIALS.column(material)) {
                val name: String = MATERIAL_MAP[prefix, material]?.getTranslatedName(langType)
                    ?: HTMaterialTranslationHelper.translate(langType, prefix, material)
                    ?: continue
                provider.add(block, name)
            }
            // Item
            for ((prefix: HTMaterialPrefix, item: HTHasTranslationKey) in HCItems.MATERIALS.column(material)) {
                val name: String = MATERIAL_MAP[prefix, material]?.getTranslatedName(langType)
                    ?: HTMaterialTranslationHelper.translate(langType, prefix, material)
                    ?: continue
                provider.add(item, name)
            }
            // Tool
            for ((toolType: HTToolType<*>, tool: HTHasTranslationKey) in HCItems.TOOLS.column(material)) {
                val name: String = toolType.translate(langType, material)
                provider.add(tool, name)
            }
        }
    }
}
