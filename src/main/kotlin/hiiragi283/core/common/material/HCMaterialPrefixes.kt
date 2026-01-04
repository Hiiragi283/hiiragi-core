package hiiragi283.core.common.material

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLanguageType
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike

object HCMaterialPrefixes {
    //    Block    //

    @JvmField
    val ORE = HTMaterialPrefix("ore")

    @JvmField
    val ORE_DEEPSLATE = HTMaterialPrefix("ore/deepslate", "deepslate_%s_ore", "c:ores")

    @JvmField
    val ORE_NETHER = HTMaterialPrefix("ore/nether", "nether_%s_ore", "c:ores")

    @JvmField
    val ORE_END = HTMaterialPrefix("ore/end", "end_%s_ore", "c:ores")

    @JvmField
    val GLASS_BLOCK = HTMaterialPrefix("glass_block")

    @JvmField
    val STORAGE_BLOCK = HTMaterialPrefix("storage_block", "%s_block")

    @JvmField
    val STORAGE_BLOCK_RAW =
        HTMaterialPrefix("storage_block/raw", "raw_%s_block", "c:storage_blocks", "c:storage_blocks/raw_%s")

    //    Item    //

    @JvmField
    val DUST = HTMaterialPrefix("dust")

    @JvmField
    val FUEL = HTMaterialPrefix("fuel", "%s")

    @JvmField
    val GEAR = HTMaterialPrefix("gear")

    @JvmField
    val GEM = HTMaterialPrefix("gem", "%s")

    @JvmField
    val INGOT = HTMaterialPrefix("ingot")

    @JvmField
    val NUGGET = HTMaterialPrefix("nugget")

    @JvmField
    val PEARL = HTMaterialPrefix("pearl")

    @JvmField
    val PLATE = HTMaterialPrefix("plate")

    @JvmField
    val RAW_MATERIAL = HTMaterialPrefix("raw_material", "raw_%s")

    @JvmField
    val ROD = HTMaterialPrefix("rod")

    @JvmField
    val SCRAP = HTMaterialPrefix("scrap")

    @JvmField
    val WIRE = HTMaterialPrefix("wire")

    @JvmField
    val TRANSLATION_MAP: Map<HTMaterialPrefix, HTLangPatternProvider> = buildMap {
        fun register(prefix: HTPrefixLike, enPattern: String, jaPattern: String) {
            this[prefix.asMaterialPrefix()] = HTLangPatternProvider { type: HTLanguageType, value: String ->
                when (type) {
                    HTLanguageType.EN_US -> enPattern
                    HTLanguageType.JA_JP -> jaPattern
                }.replace("%s", value)
            }
        }

        // Block
        register(ORE, "%s Ore", "%s鉱石")
        register(ORE_DEEPSLATE, "Deepslate %s Ore", "深層%s鉱石")
        register(ORE_NETHER, "Nether %s Ore", "ネザー%s鉱石")
        register(ORE_END, "End %s Ore", "エンド%s鉱石")

        register(STORAGE_BLOCK, "Block of %s", "%sブロック")
        register(STORAGE_BLOCK_RAW, "Block of Raw %s", "%sの原石ブロック")
        // Item
        register(DUST, "%s Dust", "%sの粉")
        register(FUEL, "%s", "%s")
        register(GEAR, "%s Gear", "%sの歯車")
        register(GEM, "%s", "%s")
        register(INGOT, "%s Ingot", "%sインゴット")
        register(NUGGET, "%s Nugget", "%sナゲット")
        register(PEARL, "%s", "%s")
        register(PLATE, "%s Plate", "%sの板")
        register(RAW_MATERIAL, "Raw %s", "%sの原石")
        register(ROD, "%s Rod", "%sの棒")
        register(SCRAP, "%s Scrap", "%sの欠片")
        register(WIRE, "%s Wire", "%sのワイヤ")
    }
}
