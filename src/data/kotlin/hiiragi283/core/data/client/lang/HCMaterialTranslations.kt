package hiiragi283.core.data.client.lang

import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems

object HCMaterialTranslations {
    @JvmField
    val PREFIX_MAP: Map<HTMaterialPrefix, HTLangPatternProvider> = buildMap {
        fun register(prefix: HTPrefixLike, enPattern: String, jaPattern: String) {
            this[prefix.asMaterialPrefix()] = HTLangPatternProvider { type: HTLanguageType, value: String ->
                when (type) {
                    HTLanguageType.EN_US -> enPattern
                    HTLanguageType.JA_JP -> jaPattern
                }.replace("%s", value)
            }
        }

        // Block
        register(HCMaterialPrefixes.STORAGE_BLOCK, "Block of %s", "%sブロック")
        register(HCMaterialPrefixes.RAW_STORAGE_BLOCK, "Block of Raw %s", "%sの原石ブロック")
        // Item
        register(HCMaterialPrefixes.DUST, "%s Dust", "%sの粉")
        register(HCMaterialPrefixes.FUEL, "%s", "%s")
        register(HCMaterialPrefixes.GEAR, "%s Gear", "%sの歯車")
        register(HCMaterialPrefixes.GEM, "%s", "%s")
        register(HCMaterialPrefixes.INGOT, "%s Ingot", "%sインゴット")
        register(HCMaterialPrefixes.NUGGET, "%s Nugget", "%sナゲット")
        register(HCMaterialPrefixes.PEARL, "%s Pearl", "%sパール")
        register(HCMaterialPrefixes.PLATE, "%s Plate", "%sの板")
        register(HCMaterialPrefixes.RAW_MATERIAL, "Raw %s", "%sの原石")
        register(HCMaterialPrefixes.ROD, "%s Rod", "%sの棒")
        register(HCMaterialPrefixes.SCRAP, "%s Scrap", "%sの欠片")
        register(HCMaterialPrefixes.WIRE, "%s Wire", "%sのワイヤ")
    }

    @JvmField
    val MATERIAL_MAP: HTTable<HTMaterialPrefix, HTMaterialKey, HTLangName> = buildTable {
        fun register(
            prefix: HTPrefixLike,
            material: HTMaterialLike,
            enName: String,
            jaName: String,
        ) {
            this[prefix.asMaterialPrefix(), material.asMaterialKey()] = HTLangName { type ->
                when (type) {
                    HTLanguageType.EN_US -> enName
                    HTLanguageType.JA_JP -> jaName
                }
            }
        }

        register(HCMaterialPrefixes.DUST, HCMaterial.Dusts.WOOD, "Sawdust", "おがくず")
        register(HCMaterialPrefixes.PLATE, HCMaterial.Plates.RUBBER, "Rubber Sheet", "ゴムシート")
        register(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.PLASTIC, "Polymer Resin", "高分子樹脂")
        register(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER, "Raw Rubber", "生ゴム")
    }

    @JvmStatic
    fun addTranslations(provider: HTLangProvider) {
        for (material: HCMaterial in HCMaterial.entries) {
            // Block
            for ((prefix: HTMaterialPrefix, block: HTHasTranslationKey) in HCBlocks.MATERIALS.column(material)) {
                val name: String = translate(provider.langType, prefix, material) ?: continue
                provider.add(block, name)
            }
            // Item
            for ((prefix: HTMaterialPrefix, item: HTHasTranslationKey) in HCItems.MATERIALS.column(material)) {
                val name: String = translate(provider.langType, prefix, material) ?: continue
                provider.add(item, name)
            }
        }
    }

    @JvmStatic
    fun translate(type: HTLanguageType, prefix: HTPrefixLike, material: HTMaterialLike): String? {
        val customName: HTLangName? = MATERIAL_MAP[prefix.asMaterialPrefix(), material.asMaterialKey()]
        if (customName != null) {
            return customName.getTranslatedName(type)
        } else {
            val translation: HTLangPatternProvider = PREFIX_MAP[prefix] ?: return null
            val translatedName: HTLangName = material as? HTLangName ?: return null
            return translation.translate(type, translatedName)
        }
    }
}
