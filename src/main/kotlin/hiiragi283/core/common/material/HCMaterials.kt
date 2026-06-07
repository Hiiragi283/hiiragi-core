package hiiragi283.core.common.material

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.material.CommonMaterialKeys
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialTranslationManager.add
import hiiragi283.lib.material.HTPartTagManager
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.tag.CommonTagPrefixes

data object HCMaterials {
    @JvmStatic
    fun initTags() {
        HTPartTagManager[CommonPartKeys.DUST] = CommonTagPrefixes.DUST
        HTPartTagManager[CommonPartKeys.GEAR] = CommonTagPrefixes.GEAR
        HTPartTagManager[CommonPartKeys.GEM] = CommonTagPrefixes.GEM
        HTPartTagManager[CommonPartKeys.INGOT] = CommonTagPrefixes.INGOT
        HTPartTagManager[CommonPartKeys.NUGGET] = CommonTagPrefixes.NUGGET
        HTPartTagManager[CommonPartKeys.RAW] = CommonTagPrefixes.RAW_MATERIALS
        HTPartTagManager[CommonPartKeys.RAW_BLOCK] = CommonTagPrefixes.RAW_STORAGE_BLOCK
        HTPartTagManager[CommonPartKeys.ROD] = CommonTagPrefixes.ROD
        HTPartTagManager[CommonPartKeys.STORAGE_BLOCK] = CommonTagPrefixes.STORAGE_BLOCK
    }

    @JvmStatic
    fun initTranslations() {
        // Part
        add(CommonPartKeys.DUST, HTLangPatternProvider.create("%s Dust", "%sの粉"))
        add(CommonPartKeys.FUEL, HTLangPatternProvider.IDENTITY)
        add(CommonPartKeys.GEAR, HTLangPatternProvider.create("%s Gear", "%sの歯車"))
        add(CommonPartKeys.GEM, HTLangPatternProvider.IDENTITY)
        add(CommonPartKeys.INGOT, HTLangPatternProvider.create("%s Ingot", "%sインゴット"))
        add(CommonPartKeys.NUGGET, HTLangPatternProvider.create("%s Nugget", "%sナゲット"))
        add(CommonPartKeys.RAW, HTLangPatternProvider.create("Raw %s", "%sの原石"))
        add(CommonPartKeys.RAW_BLOCK, HTLangPatternProvider.create("Block of Raw %s", "%sの原石ブロック"))
        add(CommonPartKeys.ROD, HTLangPatternProvider.create("%s Rod", "%sの棒"))
        add(CommonPartKeys.STORAGE_BLOCK, HTLangPatternProvider.create("Block of %s", "%sブロック"))
        // Material
        add(VanillaMaterialKeys.COAL, HTLangName.create("Coal", "石炭"))
        add(VanillaMaterialKeys.CHARCOAL, HTLangName.create("Charcoal", "木炭"))

        add(VanillaMaterialKeys.REDSTONE, HTLangName.create("Redstone", "赤石"))
        add(VanillaMaterialKeys.GLOWSTONE, HTLangName.create("Glowstone", "グロウストーン"))

        add(VanillaMaterialKeys.LAPIS, HTLangName.create("Lapis", "ラピス"))
        add(VanillaMaterialKeys.QUARTZ, HTLangName.create("Quartz", "水晶"))
        add(VanillaMaterialKeys.AMETHYST, HTLangName.create("Amethyst", "アメジスト"))
        add(VanillaMaterialKeys.DIAMOND, HTLangName.create("Diamond", "ダイヤモンド"))
        add(VanillaMaterialKeys.EMERALD, HTLangName.create("Emerald", "エメラルド"))
        add(VanillaMaterialKeys.ECHO, HTLangName.create("Echo", "残響"))
        add(VanillaMaterialKeys.PRISMARINE, HTLangName.create("Prismarine", "プリズマリン"))

        add(CommonMaterialKeys.TIN, HTLangName.create("Tin", "錫"))
        add(CommonMaterialKeys.IRIDIUM, HTLangName.create("Iridium", "イリジウム"))
        add(CommonMaterialKeys.PLATINUM, HTLangName.create("Platinum", "白金"))
        add(CommonMaterialKeys.LEAD, HTLangName.create("Lead", "鉛"))
        // Custom
        add(CommonPartKeys.DUST, VanillaMaterialKeys.WOOD, HTLangName.create("Sawdust", "おがくず"))
    }
}
