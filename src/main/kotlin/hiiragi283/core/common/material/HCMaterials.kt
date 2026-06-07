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
        // Vanilla
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

        add(VanillaMaterialKeys.COPPER, HTLangName.create("Copper", "銅"))
        add(VanillaMaterialKeys.IRON, HTLangName.create("Iron", "鉄"))
        add(VanillaMaterialKeys.GOLD, HTLangName.create("Gold", "金"))

        add(VanillaMaterialKeys.NETHERITE, HTLangName.create("Netherite", "ネザライト"))

        add(VanillaMaterialKeys.WOOD, HTLangName.create("Wooden", "木"))
        add(VanillaMaterialKeys.GLASS, HTLangName.create("Glass", "ガラス"))
        add(VanillaMaterialKeys.STONE, HTLangName.create("Stone", "石"))
        add(VanillaMaterialKeys.OBSIDIAN, HTLangName.create("Obsidian", "黒曜石"))

        add(VanillaMaterialKeys.ENDER_PEARL, HTLangName.create("Obsidian", "黒曜石"))
        add(VanillaMaterialKeys.BLAZE, HTLangName.create("Blaze", "ブレイズ"))
        add(VanillaMaterialKeys.BREEZE, HTLangName.create("Breeze", "ブリーズ"))

        add(VanillaMaterialKeys.BRICK, HTLangName.create("Brick", "レンガ"))
        add(VanillaMaterialKeys.NETHER_BRICK, HTLangName.create("Nether Brick", "ネザーレンガ"))
        // Common
        add(CommonMaterialKeys.COAL_COKE, HTLangName.create("Coal Coke", "石炭コークス"))

        add(CommonMaterialKeys.SALT, HTLangName.create("Salt", "塩"))
        add(CommonMaterialKeys.SALTPETER, HTLangName.create("Saltpeter", "硝石"))
        add(CommonMaterialKeys.BAUXITE, HTLangName.create("Bauxite", "ボーキサイト"))
        add(CommonMaterialKeys.GALENA, HTLangName.create("Galena", "方鉛鉱"))

        add(CommonMaterialKeys.AMBER, HTLangName.create("Amber", "琥珀"))
        add(CommonMaterialKeys.AQUAMARINE, HTLangName.create("Aquamarine", "アクアマリン"))
        add(CommonMaterialKeys.RUBY, HTLangName.create("Ruby", "ルビー"))
        add(CommonMaterialKeys.SAPPHIRE, HTLangName.create("Sapphire", "サファイア"))

        add(CommonMaterialKeys.ALUMINUM, HTLangName.create("Aluminum", "アルミニウム"))
        add(CommonMaterialKeys.SILICON, HTLangName.create("Silicon", "シリコン"))

        add(CommonMaterialKeys.TITANIUM, HTLangName.create("Titanium", "チタン"))
        add(CommonMaterialKeys.COBALT, HTLangName.create("Nickel", "ニッケル"))
        add(CommonMaterialKeys.NICKEL, HTLangName.create("Nickel", "ニッケル"))
        add(CommonMaterialKeys.ZINC, HTLangName.create("Zinc", "亜鉛"))

        add(CommonMaterialKeys.SILVER, HTLangName.create("Silver", "銀"))
        add(CommonMaterialKeys.TIN, HTLangName.create("Tin", "錫"))

        add(CommonMaterialKeys.IRIDIUM, HTLangName.create("Iridium", "イリジウム"))
        add(CommonMaterialKeys.PLATINUM, HTLangName.create("Platinum", "白金"))
        add(CommonMaterialKeys.LEAD, HTLangName.create("Lead", "鉛"))

        add(CommonMaterialKeys.URANIUM, HTLangName.create("Uranium", "ウラニウム"))
        add(CommonMaterialKeys.PLUTONIUM, HTLangName.create("Plutonium", "プルトニウム"))

        add(CommonMaterialKeys.STEEL, HTLangName.create("Steel", "鋼鉄"))
        add(CommonMaterialKeys.INVAR, HTLangName.create("Invar", "不変鋼"))

        add(CommonMaterialKeys.BRASS, HTLangName.create("Brass", "黄銅"))
        add(CommonMaterialKeys.CONSTANTAN, HTLangName.create("Constantan", "コンスタンタン"))
        add(CommonMaterialKeys.BRONZE, HTLangName.create("Bronze", "青銅"))

        add(CommonMaterialKeys.ELECTRUM, HTLangName.create("Electrum", "琥珀金"))
        // Custom
        add(CommonPartKeys.DUST, VanillaMaterialKeys.WOOD, HTLangName.create("Sawdust", "おがくず"))
    }
}
