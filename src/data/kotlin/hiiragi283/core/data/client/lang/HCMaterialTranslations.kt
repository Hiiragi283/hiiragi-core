package hiiragi283.core.data.client.lang

import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.HTMaterialDefinition
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.common.data.lang.HTMaterialTranslationHelper
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
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

        register(HCMaterialPrefixes.MOLTEN, HCMaterialKeys.CRIMSON_CRYSTAL, "Crimson Blood", "深紅の血液")
        register(HCMaterialPrefixes.MOLTEN, HCMaterialKeys.WARPED_CRYSTAL, "Dew of the Warp", "歪みの雫")
        register(HCMaterialPrefixes.MOLTEN, HCMaterialKeys.ELDRITCH, "Eldritch Flux", "異質な流動体")

        register(HCMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD, "Sawdust", "おがくず")
        register(HCMaterialPrefixes.PLATE, CommonMaterialKeys.RUBBER, "Rubber Sheet", "ゴムシート")
        register(HCMaterialPrefixes.RAW_MATERIAL, CommonMaterialKeys.PLASTIC, "Polymer Resin", "高分子樹脂")
        register(HCMaterialPrefixes.RAW_MATERIAL, CommonMaterialKeys.RUBBER, "Raw Rubber", "生ゴム")
    }.let(::HTMaterialTable)

    @JvmStatic
    fun addTranslations(provider: HTLangProvider) {
        val langType: HTLanguageType = provider.langType
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in HTMaterialManager.INSTANCE.entries) {
            // Block
            for ((prefix: HTMaterialPrefix, block: HTHasTranslationKey) in HCBlocks.MATERIALS.column(key)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, key, definition, MATERIAL_MAP::get) ?: continue
                provider.add(block, name)
            }
            // Fluid
            for ((prefix: HTMaterialPrefix, fluid: HTSimpleFluidContent) in HCFluids.MATERIALS.column(key)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, key, definition, MATERIAL_MAP::get) ?: continue
                provider.addFluid(fluid, name)
            }
            // Item
            for ((prefix: HTMaterialPrefix, item: HTHasTranslationKey) in HCItems.MATERIALS.column(key)) {
                val name: String = HTMaterialTranslationHelper.translate(langType, prefix, key, definition, MATERIAL_MAP::get) ?: continue
                provider.add(item, name)
            }
        }
    }
}
